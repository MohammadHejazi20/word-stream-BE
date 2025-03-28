package services

import javax.inject.{Inject, Named}
import org.apache.pekko.actor.ActorRef
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.Configuration
import actors.WebSocketHubActor
import utils.{BlogPostDiffer, JsonArrayParser, BlogFetcherUtils}
import scala.concurrent.{ExecutionContext, Future}

class WebSocketService @Inject() (
    ws: WSClient,
    config: Configuration,
    wordCounter: WordCountService,
    @Named("webSocketHub") hub: ActorRef
)(implicit ec: ExecutionContext) {

  private val blogUrl = config.get[String]("the-key-academy-url")
  private var cachedPosts: Map[Int, String] = Map.empty

  def broadcast(json: JsValue): Unit = {
    hub ! WebSocketHubActor.Broadcast(json)
  }

  def sendLatestWordCounts(): Unit = {
    BlogFetcherUtils.fetchPosts(ws, blogUrl).foreach { blogJsonString =>
      val parsedJson = JsonArrayParser.parse(blogJsonString)

      val (newOrChangedPosts, updatedCache) =
        BlogPostDiffer.detectNewOrChangedPosts(parsedJson, cachedPosts)

      // Update the cached posts
      cachedPosts = updatedCache

      // Process new or changed posts
      newOrChangedPosts match {
        case Some(filteredJson) =>
          wordCounter.countWordsFromJson(filteredJson).foreach { wordCounts =>
            hub ! WebSocketHubActor.Broadcast(wordCounts)
          }
        case None =>
          println("[info]: No new or changed posts detected.")
      }
    }
  }
}
