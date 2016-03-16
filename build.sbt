name := "Akka-PubSub-Chatroom"

version := "1.0"

scalaVersion := "2.11.7"

lazy val akkaVersion = "2.4.2"

enablePlugins(JavaAppPackaging)

libraryDependencies ++=
  Seq(
    "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test",
    "org.scalactic" %% "scalactic" % "2.2.6",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test",
    "redis.clients" % "jedis" % "2.0.0"
  )

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )