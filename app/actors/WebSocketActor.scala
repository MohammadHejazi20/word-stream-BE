package actors

import org.apache.pekko.actor._
import play.api.libs.json._
import actors.WebSocketHubActor._

object WebSocketActor {
  def props(clientRef: ActorRef, hub: ActorRef): Props =
    Props(new WebSocketActor(clientRef, hub))

  case class SendToClient(data: JsValue)
}

class WebSocketActor(clientRef: ActorRef, hub: ActorRef) extends Actor {
  override def preStart(): Unit = {
    hub ! Register(self)
    clientRef ! Json.obj("connected" -> true)
  }

  def receive: Receive = {
    case json: JsValue =>
      println(s"Received from client: $json")
    // Optionally handle messages here

    case WebSocketActor.SendToClient(data) =>
      clientRef ! data
  }
}
