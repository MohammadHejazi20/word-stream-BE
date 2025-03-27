package utils

import play.api.libs.json._

/** Utility for detecting new or changed blog posts.
  *
  * Compares a list of new blog post objects (`newPosts`) with an existing cache
  * (`oldPosts`), based on the combination of `id` and `modified`.
  *
  * Returns an optional JSON array (`Some(json)`) containing only the
  * new/changed posts, as well as an updated map for later reuse.
  *
  * @param newPosts
  *   New blog post data as Seq[JsValue]
  * @param oldPosts
  *   Current cache (Map of id -> modified)
  * @return
  *   Tuple: (Option[JsonString of changed posts], Updated map)
  */
object BlogPostDiffer {

  def detectNewOrChangedPosts(
      newPosts: Seq[JsValue],
      oldPosts: Map[Int, String]
  ): (Option[String], Map[Int, String]) = {

    // Extract posts that are new or have been modified
    val newOrUpdatedPosts = newPosts.filter { post =>
      val postId = (post \ "id").as[Int]
      val postModified = (post \ "modified").as[String]
      oldPosts.get(postId).forall(_ != postModified)
    }

    // If there are new or updated posts, update the map and return the result
    if (newOrUpdatedPosts.nonEmpty) {
      val updatedPostsMap = oldPosts ++ newOrUpdatedPosts.map { post =>
        val postId = (post \ "id").as[Int]
        val postModified = (post \ "modified").as[String]
        postId -> postModified
      }

      val newOrUpdatedJson = Json.toJson(newOrUpdatedPosts).toString()
      (Some(newOrUpdatedJson), updatedPostsMap)
    } else {
      // No new or updated posts, return the original map
      (None, oldPosts)
    }
  }
}
