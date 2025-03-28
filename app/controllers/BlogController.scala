package controllers

import play.api.mvc._
import services.BlogFetcherService
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

/** BlogController
  *
  * Exposes an endpoint to fetch raw blog post data.
  */
@Singleton
class BlogController @Inject() (
    cc: ControllerComponents,
    blogFetcher: BlogFetcherService
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def fetchBlogs: Action[AnyContent] = Action.async {
    blogFetcher.fetchPosts().map(Ok(_))
  }
}
