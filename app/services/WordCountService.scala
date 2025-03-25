package services

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import utils.{TextProcessingUtils, WordProcessingUtils}
import play.api.libs.json._

class WordCountService @Inject() ()(implicit ec: ExecutionContext) {

  /** Processes JSON response, extracts text, and counts words */
  def countWordsFromJson(jsonResponse: String): Future[JsValue] =
    Future {
      val cleanedTexts = TextProcessingUtils.extractAndCleanText(jsonResponse)
      WordProcessingUtils.countWords(cleanedTexts)
    }
}
