package misc

import twitter.{ClientContext, TweetParser}

import scala.concurrent.{ExecutionContext, Future}
import scalaj.http.Http

object JvmClientContext {
  import TweetParser._

  implicit def context(implicit ec: ExecutionContext): ClientContext = (url: String) => {
    Future {
      val json = Http(url).asString.body
      parsePayload(json)
    }
  }
}