package free

import cats.{Id, ~>}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer

class ConsoleTest extends FlatSpec with Matchers {

  def testInterpreter(input: String, outputs: ListBuffer[String]) = new (ConsoleOp ~> Id) {
    def apply[A](op: ConsoleOp[A]) = op match {
      case GetLine() => input
      case PrintLine(text) => outputs += text; ()
    }
  }

  "A program" should "ask for a name and greet the user" in {
    val outputs: ListBuffer[String] = ListBuffer.empty

    Console.program.foldMap(testInterpreter("Igal", outputs))

    outputs should be(List(
      "What is your name?",
      "Nice to meet you, Igal"
    ))
  }
}
