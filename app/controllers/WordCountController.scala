package controllers

import play.api.mvc._
import play.api.libs.json._
import services.{WordCountService, BlogFetcherService}
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
    wordCountService: WordCountService
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def fetchAndProcessBlogs: Action[AnyContent] = Action.async {
    blogFetcher.fetchPosts().flatMap { jsonResponse =>
      wordCountService.countWordsFromJson(jsonResponse).map { result =>
        Ok(result)
      }
    }
  }
}
