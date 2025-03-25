package services

import play.api.libs.ws.WSClient
import play.api.Configuration
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

/** Fetches blog posts from the specified URL.
  * @return
  *   A Future containing the response body as a String.
  * @important
  *   The response body is expected to be in JSON format.
  */

class BlogFetcherService @Inject() (ws: WSClient, appConfig: Configuration)(
    implicit ec: ExecutionContext
) {

  private val blogUrl = appConfig.get[String]("the-key-academy-url")

  def fetchPosts(): Future[String] = {
    ws.url(blogUrl).get().map { response =>
      if (response.status == 200) {
        response.body
      } else {
        throw new Exception(
          s"Failed to fetch blog posts: ${response.statusText}"
        )
      }
    }
  }
}
