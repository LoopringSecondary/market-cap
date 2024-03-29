import sbt._
import Keys._


lazy val dependencies = {
  val akkaVersion = "2.5.17"
  val akkaHttpVersion = "10.1.5"

  val logbackVersion = "1.2.3"

  Seq(
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
    "ch.qos.logback" % "logback-core" % logbackVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "org.json4s" %% "json4s-native" % "3.6.1",
    "com.thesamet.scalapb" %% "scalapb-json4s" % "0.7.1",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.22.0",
    "com.github.etaty" %% "rediscala" % "1.8.0",
    "org.jsoup" % "jsoup" % "1.11.3",
    "com.corundumstudio.socketio" % "netty-socketio" % "1.7.16",
    "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "0.20",
    "mysql" % "mysql-connector-java" % "5.1.47",
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
    "com.typesafe.slick" %% "slick-testkit" % "3.2.3" % Test,
    "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
  )
}

lazy val market_cap = (project in file("."))
  .enablePlugins(JavaServerAppPackaging)
  .enablePlugins(AutomateHeaderPlugin)
  .settings(
    name := "market-cap",
    version := "0.1",
    scalaVersion := "2.12.6",
    organization := "org.loopring",
    organizationName := "Loopring Foundation",
    startYear := Some(2018),
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    PB.targets in Compile := Seq(scalapb.gen() -> (sourceManaged in Compile).value),
    libraryDependencies ++= dependencies
  )
