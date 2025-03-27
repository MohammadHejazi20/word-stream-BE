package utils

import play.api.libs.json._
import scala.util.matching.Regex

object PerPostWordProcessor {

  def process(posts: Seq[JsValue]): JsValue = {
    // Process each post in the input sequence
    val processedPosts = posts.map { post =>
      // Extract basic fields
      val id = (post \ "id").as[Int]
      val title = (post \ "title" \ "rendered").asOpt[String].getOrElse("")
      val link = (post \ "link").asOpt[String].getOrElse("")
      val dateGmt = (post \ "date_gmt").asOpt[String].getOrElse("")
      val modifiedGmt = (post \ "modified_gmt").asOpt[String].getOrElse("")

      // === Excerpt ===
      val rawExcerpt =
        (post \ "excerpt" \ "rendered").asOpt[String].getOrElse("")
      val cleanedExcerpt = TextProcessingUtils.removeHtmlTags(rawExcerpt)
      val excerptWordCounts =
        WordProcessingUtils.countWordsFromText(cleanedExcerpt)

      // ===  Content ===
      val rawContent =
        (post \ "content" \ "rendered").asOpt[String].getOrElse("")
      val cleanedContent = TextProcessingUtils.removeHtmlTags(rawContent)
      val contentWordCounts =
        WordProcessingUtils.countWordsFromText(cleanedContent)

      // Construct JSON object for the processed post
      Json.obj(
        "id" -> id,
        "title" -> title,
        "link" -> link,
        "date_gmt" -> dateGmt,
        "modified_gmt" -> modifiedGmt,
        "contentWords" -> wordCountsToJsonArray(contentWordCounts),
        "excerptWords" -> wordCountsToJsonArray(excerptWordCounts)
      )
    }

    JsArray(processedPosts)
  }

  // Helper method to convert word counts to a JSON array
  private def wordCountsToJsonArray(wordCounts: Map[String, Int]): JsArray = {
    JsArray(
      wordCounts.map { case (word, count) =>
        Json.obj("word" -> word, "count" -> count)
      }.toSeq
    )
  }
}
