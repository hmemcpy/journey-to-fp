package free

import cats.{Id, ~>}
import org.scalajs.dom._
import twitter.TwitterClient._

import scala.collection.mutable
import scala.concurrent.Future
import scala.scalajs.js.JSApp
import scalatags.JsDom.all.{html => _, _}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import misc.JsClientContext.context
import misc.RenderUtils._

object TwitterWeb extends JSApp {
  def renderUI(list: mutable.Buffer[Node]) = new (Interact ~> Id) {
    override def apply[A](op: Interact[A]): Id[A] = op match {
      case Tell(message) => renderPrompt(list, message)
      case Ask(prompt) => renderReadPrompt(list, prompt); prompt
      case GetTweets(_) => Future(Seq())
      case DisplayTweets(_) => Future(())
    }
  }

  def renderResults(buffer: html.Div) = new (Interact ~> Id) {
    var count = -1
    override def apply[A](op: Interact[A]): Id[A] = {
      count += 1
      op match {
        case Tell(_) => ()
        case Ask(prompt) => readPrompt(count, prompt)
        case GetTweets(screenName) => getTweetsFor(screenName)
        case DisplayTweets(tweets) => renderTweets(buffer, tweets)
      }
    }
  }

  def main(): Unit = {
    val content= div(id:="content").render
    val result= div(id:="result").render

    def render(): Unit = {
      val nodeBuffer = mutable.ListBuffer.empty[Node]
      Program.twitter.foldMap(renderUI(nodeBuffer))

      val buffer = div(
        div(nodeBuffer),
        renderButton()
      ).render

      swapBuffers(content, buffer)
    }

    def fetchTweets(): Unit = {
      val buffer = div.render
      Program.twitter.foldMap(renderResults(buffer))
      swapBuffers(result, buffer)
    }

    def renderButton() = {
      val btn = button("Fetch tweets").render
      btn.onclick = (_: MouseEvent) => fetchTweets()
      btn
    }

    render()

    document.getElementById("main").appendChild(content)
    document.getElementById("main").appendChild(result)
  }
}
