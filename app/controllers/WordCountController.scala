package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.Configuration
import services.WordCountService
import javax.inject._
import scala.concurrent.ExecutionContext
import utils.BlogFetcherUtils

/** Controller for handling word count from blog posts.
  *
  * @param cc
  *   The controller components.
  * @param ws
  *   The WSClient for making HTTP requests.
  * @param blogFetcher
  *   The Utils for fetching blog posts.
  * @param wordCountService
  *   The service to count words.
  */

@Singleton
class WordCountController @Inject() (
    cc: ControllerComponents,
    ws: WSClient,
    config: Configuration,
    wordCountService: WordCountService
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  private val blogUrl = config.get[String]("the-key-academy-url")

  def fetchAndProcessBlogs: Action[AnyContent] = Action.async {
    BlogFetcherUtils.fetchPosts(ws, blogUrl).flatMap { jsonResponse =>
      wordCountService.countWordsFromJson(jsonResponse).map { result =>
        Ok(result)
      }
    }
  }
}
