package controllers

import play.api.mvc._
import services.{WordCountService, BlogFetcherService}
import javax.inject._
import scala.concurrent.ExecutionContext
import play.api.libs.json._
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer

/** Controller for handling word count from blog posts.
  *
  * @param cc
  *   The controller components.
  * @param blogFetcher
  *   The service to fetch blog posts.
  * @param wordCountService
  *   The service to count words.
  */

@Singleton
class WordCountController @Inject() (
    cc: ControllerComponents,
    blogFetcher: BlogFetcherService,
    webSocketController: WebSocketController // Inject the WebSocketController
)(implicit system: ActorSystem, ec: ExecutionContext, mat: Materializer)
    extends AbstractController(cc) {

  // Access the hub actor through the WebSocketController
  private val hub = webSocketController.getHub
  private val wordCountService = new WordCountService(hub)

  def fetchAndProcessBlogs: Action[AnyContent] = Action.async {
    blogFetcher.fetchPosts().flatMap { jsonResponse =>
      wordCountService.countWordsFromJson(jsonResponse).map { result =>
        Ok(result)
      }
    }
  }
}
