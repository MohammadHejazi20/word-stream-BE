package actors

import org.apache.pekko.actor._
import play.api.libs.json._
import actors.WebSocketHubActor._
import services.WebSocketService

object WebSocketActor {
  def props(
      client: ActorRef,
      hub: ActorRef,
      webSocketService: WebSocketService
  ): Props = Props(new WebSocketActor(client, hub, webSocketService))

  case class SendToClient(data: JsValue)
}

class WebSocketActor(
    client: ActorRef,
    hub: ActorRef,
    webSocketService: WebSocketService
) extends Actor {

  // === Lifecycle Hook: On Connect
  override def preStart(): Unit = {
    // Register with the hub
    hub ! Register(self)

    // Send the last cached word count to the client (if available)
    webSocketService.getLastBroadcasted().foreach { cachedData =>
      client ! cachedData
    }
  }

  // === Incoming Messages
  def receive: Receive = {
    case WebSocketActor.SendToClient(data) =>
      client ! data

    case msg: JsValue =>
      println(
        s"[WebSocketActor] Message from client: $msg"
      )
  }
}
