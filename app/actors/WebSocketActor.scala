package actors

import org.apache.pekko.actor._
import play.api.libs.json._
import actors.WebSocketHubActor._

object WebSocketActor {
  def props(
      client: ActorRef,
      hub: ActorRef
  ): Props = Props(new WebSocketActor(client, hub))

  case class SendToClient(data: JsValue)
}

class WebSocketActor(
    client: ActorRef,
    hub: ActorRef
) extends Actor {

  // === Lifecycle Hook: On Connect
  override def preStart(): Unit = {
    hub ! Register(self)
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
