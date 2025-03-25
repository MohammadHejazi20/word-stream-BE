package utils

import play.api.libs.json._

object WordProcessingUtils {

  /** Counts word occurrences in a given text and returns JSON */
  def countWords(texts: Seq[String]): JsValue = {
    val wordCounts = texts
      .mkString(" ") // Combine all posts into one text
      .toLowerCase
      .split("\\W+") // Split by non-word characters
      .filter(_.nonEmpty) // Remove empty strings
      .groupBy(identity) // Group by word
      .map { case (word, occurrences) =>
        Json.obj(word -> occurrences.length)
      } // cn to JSON object

    Json.toJson(wordCounts.toSeq) // cn map to JSON array
  }
}
