package services

import javax.inject.{Inject, Named}
import org.apache.pekko.actor.ActorRef
import play.api.libs.json._
import actors.WebSocketHubActor

class WebSocketService @Inject() (@Named("webSocketHub") hub: ActorRef) {

  def broadcast(json: JsValue): Unit = {
    hub ! WebSocketHubActor.Broadcast(json)
  }
}
