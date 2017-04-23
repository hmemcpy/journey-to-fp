package twitter

import com.typesafe.config._
import net.ceedubs.ficus.Ficus._
import scala.concurrent.duration._
import org.http4s.server.middleware.CORSConfig
import scalaj.http.Token

trait ServerConfig {
  val corsConfig = CORSConfig(
    anyOrigin = true,
    anyMethod = true,
    allowCredentials = true,
    maxAge = 1.day.toSeconds)

  val config = ConfigFactory.load()
  val appKey = config.as[String]("twitter.consumer-key")
  val appSecret = config.as[String]("twitter.consumer-secret")
  val accessTokenKey = config.as[String]("twitter.access-token")
  val accessTokenSecret = config.as[String]("twitter.access-token-secret")
  val port = config.as[Int]("http.port")

  val consumerToken = Token(appKey, appSecret)
  val accessToken = Token(accessTokenKey, accessTokenSecret)
}
