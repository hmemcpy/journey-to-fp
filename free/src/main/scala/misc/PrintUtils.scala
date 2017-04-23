package misc

import twitter.Tweet

import scala.concurrent._

object PrintUtils {
  def printTweets(tweets: Future[Seq[Tweet]])(implicit ec: ExecutionContext): Future[Unit] =
  tweets map printTweets

  def printTweets(tweets: Seq[Tweet]) =
    tweets foreach { tweet =>
      printTweet(tweet)
      println()
    }

  def printTweet(tweet: Tweet): Unit = {
    import math._
    val width = 60
    def border() = println(s"+" + ("-" * (width - 2)) + "+")
    def blankLine() = println(withEllipsis("| ", "", " |"))
    def entry(value: String) = println(withEllipsis("| ", value, " |"))
    def entryWrapped(value: String) = println(wrap("| ", value, " |"))
    def withoutEllipsis(marginLeft: String, value: String, marginRight: String): String = {
      val availableWidth = width - marginLeft.length - marginRight.length
      val fill = max(0, availableWidth - value.length)
      marginLeft + value + (" " * fill) + marginRight
    }
    def withEllipsis(marginLeft: String, value: String, marginRight: String): String = {
      val availableWidth = width - marginLeft.length - marginRight.length
      val remainder = availableWidth - value.length

      val modified =
        if (remainder < 0) {
          val amountToStrip = abs(remainder) + "...".length
          value.dropRight(amountToStrip) + "..."
        } else {
          value
        }

      withoutEllipsis(marginLeft, modified, marginRight)
    }

    def wrap(marginLeft: String, value: String, marginRight: String): String = {
      val maxLength = width - marginLeft.length - marginRight.length
      val (wrapped, wrappedLine, _) = value.split(" ").foldLeft ((Seq.empty[String], "", 0)) {
        case (acc @ (seq, line, soFar), nextWord) =>
          if (nextWord equals "") {
            acc
          } else if ((soFar + " ".length + nextWord.length) < maxLength) {
            (seq, line + " " + nextWord, soFar + " ".length + nextWord.length)
          } else {
            (seq :+ line.trim, nextWord, nextWord.length)
          }
      }
      val result = wrapped :+ wrappedLine.trim

      result map (withoutEllipsis(marginLeft, _, marginRight)) mkString "\n"
    }

    border()
    entry(s"${tweet.user.name} (@${tweet.user.screen_name})")
    blankLine()
    entryWrapped(tweet.text)
    border()
  }
}
