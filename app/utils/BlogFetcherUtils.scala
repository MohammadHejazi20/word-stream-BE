package utils

import play.api.libs.ws.WSClient
import scala.concurrent.{ExecutionContext, Future}

/** BlogFetcherUtils
  *
  * Utility object to fetch blog posts from a given URL using Play's WSClient.
  * NOTE: This is not using Dependency Injection. You must pass WSClient
  * manually.
  */
object BlogFetcherUtils {

  /** Fetches blog posts as raw JSON string from a given URL.
    *
    * @param ws
    *   The Play WSClient to use for the HTTP call
    * @param url
    *   The URL to fetch blog posts from
    * @param ec
    *   ExecutionContext for async operations
    * @return
    *   Future containing the raw response body
    */
  def fetchPosts(ws: WSClient, url: String)(implicit
      ec: ExecutionContext
  ): Future[String] = {
    ws.url(url).get().map { response =>
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
