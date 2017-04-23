package twitter

import scala.concurrent.Future

object TwitterClient {
  def getTweetsFor(handle: String)(implicit context: ClientContext): Future[Seq[Tweet]] =
    Request.tweets(handle)
}

object Request {
  def tweets(handle: String)(implicit client: ClientContext): Future[Seq[Tweet]] = {
    val url = s"http://localhost:9876/tweets/$handle"
    client.getTweets(url)
  }
}