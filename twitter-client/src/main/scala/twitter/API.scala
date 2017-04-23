package twitter

import io.circe._
import io.circe.parser.decode

case class User(name: String, screen_name: String, profile_image_url: String)
case class Tweet(id_str: String, text: String, user: User) {
  val url = s"https://twitter.com/${user.screen_name}/status/${id_str}"
}

object TweetParser {
  def parsePayload(json: String): Seq[Tweet] = {
    decode[Seq[Tweet]](json) match {
      case Left(error) => println(s"Unable to parse JSON... error was: $error"); Seq.empty
      case Right(result) => result
    }
  }

  implicit val decodeUser: Decoder[User] =
    Decoder.forProduct3("name", "screen_name", "profile_image_url")(User.apply)

  implicit val decodeTweet: Decoder[Tweet] =
    Decoder.forProduct3("id_str", "text", "user")(Tweet.apply)
}