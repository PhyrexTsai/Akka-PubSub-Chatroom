name := "Akka-PubSub-Chatroom"

version := "1.0"

scalaVersion := "2.11.7"

lazy val akkaVersion = "2.4.2"

enablePlugins(JavaAppPackaging)

libraryDependencies ++=
  Seq(
    "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion
  )

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )