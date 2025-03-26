import com.google.inject.AbstractModule
import play.api.libs.concurrent.PekkoGuiceSupport

class Module extends AbstractModule with PekkoGuiceSupport {
  override def configure(): Unit = {}
}
