package utils

import play.api.libs.json._
import scala.util.matching.Regex

object PerPostWordProcessor {

  def process(posts: Seq[JsValue]): JsValue = {
    val processedPosts = posts.map { post =>
      val id = (post \ "id").as[Int]
      val title = (post \ "title" \ "rendered").asOpt[String].getOrElse("")
      val link = (post \ "link").asOpt[String].getOrElse("")
      val dateGmt = (post \ "date_gmt").asOpt[String].getOrElse("")
      val modifiedGmt = (post \ "modified_gmt").asOpt[String].getOrElse("")
      val rawContent =
        (post \ "content" \ "rendered").asOpt[String].getOrElse("")

      val cleanedContent = TextProcessingUtils.removeHtmlTags(rawContent)
      val wordCounts = WordProcessingUtils.countWordsFromText(cleanedContent)

      Json.obj(
        "id" -> id,
        "title" -> title,
        "link" -> link,
        "date_gmt" -> dateGmt,
        "modified_gmt" -> modifiedGmt,
        "words" -> JsArray(
          wordCounts.map { case (word, count) =>
            Json.obj("word" -> word, "count" -> count)
          }.toSeq
        )
      )
    }

    JsArray(processedPosts)
  }
}
