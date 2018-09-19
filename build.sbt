name := "market-cap"

version := "0.1"

scalaVersion := "2.12.6"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

libraryDependencies ++= {
  val akkaVersion = "2.5.16"
  val akkaHttpVersion = "10.1.5"
  Seq(
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "com.github.etaty" %% "rediscala" % "1.8.0",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
  )
}
