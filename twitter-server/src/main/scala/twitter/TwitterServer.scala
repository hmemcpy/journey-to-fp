package twitter

import fs2.Task
import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.middleware.CORS
import org.http4s.util._

import scalaj.http.Http

object TwitterServer extends StreamApp with ServerConfig {
  override def main(args: List[String]) =
    BlazeBuilder.bindHttp(port)
    .mountService(CORS(service, corsConfig), "/")
    .serve

  def getTweetsFor(handle: String): Task[String] = {
    Task.now(
      Http("https://api.twitter.com/1.1/statuses/user_timeline.json")
        .param("screen_name", handle)
        .param("count", "5")
        .oauth(consumerToken, accessToken)
        .asString.body
    )
  }

  val service = HttpService {
    case GET -> Root => Ok(getTweetsFor("WixEng"))
    case GET -> Root / "tweets" / handle => Ok(getTweetsFor(handle))
  }
}
