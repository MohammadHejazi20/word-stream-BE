package controllers

import play.api.mvc._
import services.BlogFetcherService
import javax.inject.Inject
import scala.concurrent.ExecutionContext

/** Controller for handling blog-related requests.
  *
  * @param cc
  *   The controller components.
  * @param blogFetcher
  *   The service to fetch blog posts.
  */
class BlogController @Inject() ( cc: ControllerComponents, blogFetcher: BlogFetcherService)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def fetchBlogs: Action[AnyContent] = Action.async {
    blogFetcher
      .fetchPosts()
      .map((v) => Ok(v))
  }
}
