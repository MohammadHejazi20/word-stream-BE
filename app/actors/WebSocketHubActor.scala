package actors

import org.apache.pekko.actor._
import play.api.libs.json.JsValue
import actors.WebSocketActor.SendToClient

class WebSocketHubActor extends Actor {
  private var clients = List.empty[ActorRef]

  def receive: Receive = {
    case WebSocketHubActor.Register(client) =>
      clients ::= client

    case WebSocketHubActor.Broadcast(data) =>
      clients.foreach(_ ! SendToClient(data))
  }
}

object WebSocketHubActor {
  case class Register(client: ActorRef)
  case class Broadcast(data: JsValue)
}
