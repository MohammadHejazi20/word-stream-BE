package utils

import play.api.libs.json._
import scala.util.Try

/** JsonArrayParser to make my life easier when dealing with JSON arrays.
  *
  * Utility object to parse a JSON array string into a sequence of JsValue.
  */
object JsonArrayParser {

  def parse(json: String): Seq[JsValue] = {
    try {
      val parsedJson = Json.parse(json)
      parsedJson match {
        case array: JsArray => array.value.toSeq
        case _ =>
          println("[JsonArrayParser]: Input is not a valid JSON array")
          Seq.empty
      }
    } catch {
      case e: Exception =>
        println(
          s"[JsonArrayParser]: Failed to parse JSON. Error: ${e.getMessage}"
        )
        Seq.empty
    }
  }
}
