import com.google.inject.AbstractModule
import play.api.libs.concurrent.PekkoGuiceSupport
import actors.WebSocketHubActor

// This module sets up dependency injection for the application. It binds the
// WebSocketHubActor to the name "webSocketHub" using PekkoGuiceSupport.

class Module extends AbstractModule with PekkoGuiceSupport {
  override def configure(): Unit = {
    bindActor[WebSocketHubActor]("webSocketHub")
  }
}
