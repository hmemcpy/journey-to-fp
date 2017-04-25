package misc

import org.scalajs.dom
import org.scalajs.dom._
import twitter.Tweet

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scalatags.JsDom.all.{input, html => _, _}

object RenderUtils {
  def renderTweets(buffer: html.Div, futureTweets: Future[Seq[Tweet]])(implicit ec: ExecutionContext): Future[Unit] =
    futureTweets map { tweets =>
      tweets.foreach(tweet => buffer.appendChild(renderTweet(tweet).render))
    }

  def renderTweet(tweet: Tweet) = {
    blockquote(
      `class` := "twitter-tweet",
      div(
        `class` := "Tweet-header",
        div(
          `class` := "Tweet-author",
          a(
            `class` := "Tweet-authorLink",
            href := s"https://twitter.com/${tweet.user.screen_name}",
            span(
              `class` := "Tweet-authorAvatar",
              img(
                `class` := "Avatar",
                src := tweet.user.profile_image_url
              )
            ),
            span(
              `class` := "Tweet-authorName",
              tweet.user.name
            ),
            span(
              `class` := "Tweet-authorScreenName",
              s"@${tweet.user.screen_name}"
            )
          )
        )
      ),
      p(
        a(
          href := s"${tweet.url}",
          tweet.text
        )
      )
    )
  }

  def inputValueFor(id: String, default: String): String = {
    inputValueFor(id).getOrElse(default)
  }

  def inputValueFor(id: String): Option[String] = {
    val maybeElement = Option(dom.document.getElementById(id))
    val maybeValue = maybeElement.map(_.asInstanceOf[html.Input].value).filter(_.nonEmpty)
    maybeValue
  }

  def swapBuffers(target: html.Div, buffer: html.Div): Unit = {
    target.innerHTML = ""
    target.appendChild(buffer)
  }

  def readPrompt(index: Int, default: String = ""): String =
    inputValueFor(s"text$index", default)

  def renderPrompt(list: mutable.Buffer[Node], prompt: String): Unit = {
    list +=
      div(
        `class` := "label",
        prompt
      ).render
  }

  def renderReadPrompt(list: mutable.Buffer[Node], default: String = ""): Unit = {
    val fieldId = s"text${list.size}"
    list +=
      label(
        `for` := fieldId,
        default,
        br.render
      ).render

    list +=
      input(
        `type` := "text",
        id := fieldId
      ).render
  }
}
