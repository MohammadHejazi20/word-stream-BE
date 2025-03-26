package controllers

import actors.{WebSocketActor, WebSocketHubActor}
import services.{BlogFetcherService, WordCountService}
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.libs.json.JsValue
import org.apache.pekko.actor.{ActorRef, ActorSystem, Props}
import org.apache.pekko.stream.Materializer
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class WebSocketController @Inject() (
    cc: ControllerComponents,
    ws: play.api.libs.ws.WSClient,
    config: play.api.Configuration
)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext)
    extends AbstractController(cc) {

  // === Create central hub actor
  private val hub: ActorRef =
    system.actorOf(Props[WebSocketHubActor], "webSocketHub")

  // === Services used by each socket connection
  private val blogFetcher = new BlogFetcherService(ws, config)
  private val wordCounter = new WordCountService(hub)

  def websocket: WebSocket = WebSocket.accept[JsValue, JsValue] { _ =>
    ActorFlow.actorRef { clientRef =>
      // Every connection gets its own socket actor
      WebSocketActor.props(clientRef, hub, blogFetcher, wordCounter)
    }
  }

  // Optional getter if needed somewhere else
  def getHub: ActorRef = hub
}
