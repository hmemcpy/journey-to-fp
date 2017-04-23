package free

import cats._
import cats.free._

sealed trait ConsoleOp[A]
case class GetLine() extends ConsoleOp[String]
case class PrintLine(text: String) extends ConsoleOp[Unit]

object ConsoleOp {
  def getLine: Free[ConsoleOp, String] = Free.liftF(GetLine())
  def printLine(text: String): Free[ConsoleOp, Unit] = Free.liftF(PrintLine(text))
}

object ConsoleInterpreter extends (ConsoleOp ~> Id) {
  def apply[A](consoleOp: ConsoleOp[A]): Id[A] = consoleOp match {
    case GetLine() => scala.io.StdIn.readLine
    case PrintLine(text) => scala.Console.println(text)
  }

  def run(program: Free[ConsoleOp, Unit]) = program.foldMap(this)
}

object Console {
  import ConsoleOp._

  val program: Free[ConsoleOp, Unit] = for {
    _ <- printLine("What is your name?")
    name <- getLine
    _ <- printLine(s"Nice to meet you, $name")
  } yield ()
}

object ConsoleApp extends App {
  import Console._
  ConsoleInterpreter.run(program)
}