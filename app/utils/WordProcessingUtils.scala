package utils

import play.api.libs.json._

object WordProcessingUtils {

  /** Counts words in raw string and returns Map[word -> count] */
  def countWordsFromText(text: String): Map[String, Int] = {
    text.toLowerCase
      .split("[^\\p{L}\\p{Nd}]+") // Split by non-word characters
      .filter(_.nonEmpty)
      .groupBy(identity)
      .view
      .mapValues(_.length)
      .toMap
  }
}
