package free

import cats.free.Free
import cats.{Id, ~>}
import twitter.TwitterClient._
import twitter._
import misc.PrintUtils._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import misc.JvmClientContext.context

sealed trait Interact[A]
case class Ask(prompt: String) extends Interact[String]
case class Tell(msg: String) extends Interact[Unit]
case class GetTweets(handle: String) extends Interact[Future[Seq[Tweet]]]
case class DisplayTweets(tweets: Future[Seq[Tweet]]) extends Interact[Future[Unit]]

object TwitterDSL {
  def ask(prompt: String): Free[Interact, String] = Free.liftF(Ask(prompt))
  def tell(msg: String): Free[Interact, Unit] = Free.liftF(Tell(msg))
  def getTweets(handle: String): Free[Interact, Future[Seq[Tweet]]] = Free.liftF(GetTweets(handle))
  def displayTweets(tweets: Future[Seq[Tweet]]): Free[Interact, Future[Unit]] = Free.liftF(DisplayTweets(tweets))
}

object Program {
  import TwitterDSL._

  val twitter = for {
    _      <- tell("Welcome to Free Twitter!")
    handle <- ask("Please enter a twitter username:")
    tweets <- getTweets(handle.stripPrefix("@"))
      done <- displayTweets(tweets)
  } yield done
}

object TwitterApp extends App {
  Await.result(Program.twitter.foldMap(TwitterInterpreter), 1.minute)
}

object TwitterInterpreter extends (Interact ~> Id) {
  override def apply[A](op: Interact[A]): Id[A] = op match {
    case Tell(msg) => println(msg)
    case Ask(prompt) => println(prompt); scala.io.StdIn.readLine
    case GetTweets(handle) => getTweetsFor(handle)
    case DisplayTweets(tweets) => printTweets(tweets)
  }
}
