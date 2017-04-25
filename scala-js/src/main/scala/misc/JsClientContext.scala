package misc

import twitter.{ClientContext, TweetParser}

import scala.concurrent.{ExecutionContext, Future}

object JsClientContext {
  import TweetParser._

  implicit def context(implicit ec: ExecutionContext): ClientContext = (url: String) => {
    import org.scalajs.dom
    dom.ext.Ajax.get(url = url)
      .map(_.responseText)
      .flatMap { json =>
        Future {
          parsePayload(json)
        }
      }
  }
}
