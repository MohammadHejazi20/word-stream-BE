package services

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import play.api.libs.json._
import utils.{PerPostWordProcessor, JsonArrayParser}

class WordCountService @Inject() ()(implicit ec: ExecutionContext) {

  /** Processes a JSON blog post list and returns word count per post */
  def countWordsFromJson(jsonResponse: String): Future[JsValue] = Future {
    val posts = JsonArrayParser.parse(jsonResponse)

    // Count words per post and return as JSON
    PerPostWordProcessor.process(posts)
  }
}
