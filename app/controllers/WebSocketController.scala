package controllers

import actors.{WebSocketActor, WebSocketHubActor}
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.libs.json.JsValue
import javax.inject.{Inject, Singleton}
import org.apache.pekko.actor.{ActorRef, ActorSystem, Props}
import org.apache.pekko.stream.Materializer

@Singleton
class WebSocketController @Inject() (
    cc: ControllerComponents
)(implicit system: ActorSystem, mat: Materializer)
    extends AbstractController(cc) {

  // Instantiate the WebSocketHubActor once
  private val hub: ActorRef =
    system.actorOf(Props[WebSocketHubActor], "webSocketHub")

  def websocket: WebSocket = WebSocket.accept[JsValue, JsValue] { _ =>
    ActorFlow.actorRef { clientRef =>
      WebSocketActor.props(clientRef, hub)
    }
  }

  // Provide a method to access the hub actor
  def getHub: ActorRef = hub
}
