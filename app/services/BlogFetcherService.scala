package services

import play.api.libs.ws.WSClient
import play.api.Configuration
import utils.BlogFetcherUtils
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

/** BlogFetcherService
  *
  * Wrapper around BlogFetcherUtils that fetches blog posts using a URL
  * configured in application.conf.
  */
class BlogFetcherService @Inject() (
    ws: WSClient,
    config: Configuration
)(implicit ec: ExecutionContext) {

  private val blogUrl: String = config.get[String]("the-key-academy-url")

  /** Fetches blog posts using WSClient and a configured URL.
    * @return
    *   Future[String] containing raw JSON string
    */
  def fetchPosts(): Future[String] = {
    BlogFetcherUtils.fetchPosts(ws, blogUrl)
  }
}
