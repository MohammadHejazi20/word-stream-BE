package utils

import play.api.libs.json._
import scala.util.matching.Regex
import scala.collection.immutable.Seq

/** Utility object for processing text */
object TextProcessingUtils {

  /** Removes HTML tags and unwanted characters from a string */
  def removeHtmlTags(text: String): String = {
    // Define a regex pattern to match HTML tags, entities, and special characters
    val htmlAndSpecialCharsPattern =
      """<[^>]*>|(&[^;\s]+;)|[!@#$%^&*„“;"()\[\]]|(\\r\\n)+|\\r+|\\n+|\\t+"""

    text
      // Remove HTML tags, entities, and special characters
      .replaceAll(htmlAndSpecialCharsPattern, "")
      // Replace multiple whitespace characters with a single space
      .replaceAll("\\s+", " ")
      // Trim leading and trailing whitespace
      .trim
  }
}
