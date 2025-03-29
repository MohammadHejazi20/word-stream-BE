package controllers

import actors.{WebSocketActor}
import services.{WebSocketService}
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.libs.json.JsValue
import org.apache.pekko.actor.{ActorRef, ActorSystem}
import org.apache.pekko.stream.Materializer
import javax.inject.{Inject, Named, Singleton}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton
class WebSocketController @Inject() (
    cc: ControllerComponents,
    webSocketService: WebSocketService,
    @Named("webSocketHub") hub: ActorRef // Is automatically injected by Guice
)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext)
    extends AbstractController(cc) {

  // Schedule a task to send the latest word counts to the hub every 5 seconds
  system.scheduler.scheduleAtFixedRate(0.seconds, 5.seconds) { () =>
    webSocketService.sendLatestWordCounts()
  }

  def websocket: WebSocket = WebSocket.accept[JsValue, JsValue] { _ =>
    ActorFlow.actorRef { clientRef =>
      // Every connection gets its own socket actor
      WebSocketActor.props(clientRef, hub, webSocketService)
    }
  }
}
