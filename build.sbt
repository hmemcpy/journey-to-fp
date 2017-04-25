
name := "Journey to FP"
scalaVersion in ThisBuild := "2.12.1"

addCommandAlias("run-twitter", "; project twitter-server ; run")

lazy val root = (project in file("."))
  .aggregate(free, `twitter-client`, `twitter-server`, `scala-js`)

lazy val free = project
  .dependsOn(`twitter-client`)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats"       % "0.9.0",
      "org.scalaj"    %% "scalaj-http" % "2.3.0",
      "org.scalatest" %% "scalatest"   % "3.0.1" % "test"
    )
  ).enablePlugins(ScalaJSPlugin)

lazy val `twitter-client` = project
  .settings(
    libraryDependencies ++= Seq(
      "org.scalaj" %% "scalaj-http" % "2.3.0",

      "io.circe" %% "circe-core"    % "0.7.1",
      "io.circe" %% "circe-generic" % "0.7.1",
      "io.circe" %% "circe-parser"  % "0.7.1"
    )
  ).enablePlugins(ScalaJSPlugin)

lazy val `twitter-server` = project
  .dependsOn(`twitter-client`)
  .settings(
    libraryDependencies ++= {
      val http4sVersion = "0.17.0-M1"
      Seq(
        "org.http4s" %% "http4s-blaze-server" % http4sVersion,
        "org.http4s" %% "http4s-circe" % http4sVersion,
        "org.http4s" %% "http4s-dsl" % http4sVersion,

        "com.typesafe" % "config" % "1.3.1",
        "com.iheart" %% "ficus" % "1.4.0"
      )
    }
  )

lazy val `scala-js` = project
  .dependsOn(`twitter-client`, free)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.lihaoyi"  %%% "scalatags"   % "0.6.3",

      "io.circe" %%% "circe-core"    % "0.7.1",
      "io.circe" %%% "circe-generic" % "0.7.1",
      "io.circe" %%% "circe-parser"  % "0.7.1"
    ),
    scalaJSUseMainModuleInitializer := true
  ).enablePlugins(ScalaJSPlugin)
