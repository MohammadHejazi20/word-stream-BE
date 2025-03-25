package utils

import play.api.libs.json._
import scala.util.matching.Regex
import scala.collection.immutable.Seq

/** Utility object for processing text */
object TextProcessingUtils {

  def extractAndCleanText(jsonResponse: String): Seq[String] = {
    val json = Json.parse(jsonResponse)

    // Extract "rendered" field from "content" and explicitly convert to immutable Seq
    val extractedTexts: Seq[String] = (json \\ "content")
      .map { content =>
        (content \ "rendered").asOpt[String].getOrElse("")
      }
      .to(Seq)

    extractedTexts.map(removeHtmlTags)
  }

  /** Removes HTML tags from a string */
  def removeHtmlTags(text: String): String = {
    val htmlTagPattern: Regex = "<[^>]+>".r // Regex to match HTML tags
    htmlTagPattern.replaceAllIn(text, "").trim // Remove HTML tags
  }
}
