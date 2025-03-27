package controllers

import actors.{WebSocketActor}
import services.{BlogFetcherService, WordCountService}
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.libs.json.JsValue
import org.apache.pekko.actor.{ActorRef, ActorSystem}
import org.apache.pekko.stream.Materializer
import javax.inject.{Inject, Named, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class WebSocketController @Inject() (
    cc: ControllerComponents,
    blogFetcher: BlogFetcherService,
    wordCounter: WordCountService,
    @Named("webSocketHub") hub: ActorRef // Is automatically injected by Guice
)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext)
    extends AbstractController(cc) {

  def websocket: WebSocket = WebSocket.accept[JsValue, JsValue] { _ =>
    ActorFlow.actorRef { clientRef =>
      // Every connection gets its own socket actor
      WebSocketActor.props(clientRef, hub, blogFetcher, wordCounter)
    }
  }

  // Optional getter if needed somewhere else
  def getHub: ActorRef = hub
}
