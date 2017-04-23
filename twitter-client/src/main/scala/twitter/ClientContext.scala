package twitter

import scala.concurrent.Future

trait ClientContext {
  def getTweets(url: String): Future[Seq[Tweet]]
}