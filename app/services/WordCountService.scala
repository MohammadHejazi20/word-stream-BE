package services

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import utils.{TextProcessingUtils, WordProcessingUtils}
import play.api.libs.json._
import services.WebSocketService

class WordCountService @Inject() (
    webSocketService: WebSocketService
)(implicit ec: ExecutionContext) {

  /** Processes JSON response, extracts text, and counts words */
  def countWordsFromJson(jsonResponse: String): Future[JsValue] =
    Future {
      val cleanedTexts = TextProcessingUtils.extractAndCleanText(jsonResponse)
      val result = WordProcessingUtils.countWords(cleanedTexts)

      // Send JSON result directly to all connected WebSocket clients
      webSocketService.broadcast(result)

      result
    }
}
