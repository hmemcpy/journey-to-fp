
name := "Journey to FP"
scalaVersion in ThisBuild := "2.12.1"

lazy val root = (project in file("."))
  .aggregate(free)

lazy val free = project
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats"       % "0.9.0",
      "org.scalaj"    %% "scalaj-http" % "2.3.0",
      "org.scalatest" %% "scalatest"   % "3.0.1" % "test"
    )
  ).enablePlugins(ScalaJSPlugin)