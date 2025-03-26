package controllers

import play.api.mvc._
import play.api.libs.json._
import services.{WordCountService, BlogFetcherService}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import javax.inject._
import scala.concurrent.ExecutionContext

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

  // === Shared actor hub from WebSocketController
  private val hub = webSocketController.getHub

  // === Instantiate the WordCountService with the shared hub (sends to hub)
  private val wordCountService = new WordCountService(hub)

  def fetchAndProcessBlogs: Action[AnyContent] = Action.async {
    blogFetcher.fetchPosts().flatMap { jsonResponse =>
      wordCountService.countWordsFromJson(jsonResponse).map { result =>
        Ok(result)
      }
    }
  }
}
