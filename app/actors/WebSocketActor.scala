package actors

import services.{BlogFetcherService, WordCountService}

import org.apache.pekko.actor._
import org.apache.pekko.actor.Cancellable

import play.api.libs.json._
import actors.WebSocketHubActor._
import scala.concurrent.duration._

object WebSocketActor {
  def props(
      client: ActorRef,
      hub: ActorRef,
      blogFetcher: BlogFetcherService,
      wordCounter: WordCountService
  ): Props = Props(new WebSocketActor(client, hub, blogFetcher, wordCounter))

  case class SendToClient(data: JsValue)
}

class WebSocketActor(
    client: ActorRef,
    hub: ActorRef,
    blogFetcher: BlogFetcherService,
    wordCounter: WordCountService
) extends Actor {

  import context.dispatcher

  private var interval: Option[Cancellable] = None
  private var lastModified: String = ""

  // === Lifecycle Hook: On Connect
  override def preStart(): Unit = {
    hub ! Register(self) // Register with master
    client ! Json.obj("connected" -> true) // Notify client
    startSendingUpdates() // Start polling every 5 seconds
  }

  // === Lifecycle Hook: On Disconnect
  override def postStop(): Unit = {
    stopSendingUpdates() // Clean up
  }

  // === Incoming Messages
  def receive: Receive = {
    case WebSocketActor.SendToClient(data) =>
      client ! data
    case msg: JsValue =>
      println(s"[WebSocket] Got from client: $msg")
  }

  // === Polling every 5 seconds
  private def startSendingUpdates(): Unit = {
    interval = Some(
      context.system.scheduler.scheduleAtFixedRate(0.seconds, 5.seconds) { () =>
        sendLatestWordCounts()
      }
    )
  }

  private def stopSendingUpdates(): Unit = {
    interval.foreach(_.cancel())
  }

  // === Data fetch + emit to client
  private def sendLatestWordCounts(): Unit = {
    blogFetcher.fetchPosts().foreach { blogJsonSting =>
      if (hasUpdates(blogJsonSting)) {
        wordCounter.countWordsFromJson(blogJsonSting).foreach(client ! _)
      } else {
        println("[info]:  No new updates found")
      }
    }
  }

  // === Check for updates
  private def hasUpdates(jsonString: String): Boolean = {
    val json = Json.parse(jsonString)

    val newestModified = (json \\ "modified")
      .flatMap(_.asOpt[String])
      .sorted
      .lastOption
      .getOrElse("")

    val changed = newestModified != lastModified

    if (changed) {
      lastModified = newestModified
    }

    changed
  }
}
