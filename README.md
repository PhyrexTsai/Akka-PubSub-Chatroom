# Akka-PubSub-Chatroom

## Introduction
This project use Scala and Akka build a Publish/Subscribe Chatroom.
Redis for handle all members of each topic.

##### This Chatroom include :
* Choosable Topic.
* Group chat.
* Member list for each topic.
* Boardcast normal messages.
* Boardcasting limit time message (Message will expire after 10 second).

## Backend
* Scala : Core language.
* Akka : Handle user's chat message delivery and Chatroom system.
* Redis : Handle the member list with each topic.


## Frontend
* HTML : Chat room layout.
* CSS : Chat room style.
* JavaScript : Handle chat room websocket and reactive with users.

## Server
Heroku : It's a PaaS platform.
More info here : [Heroku](https://devcenter.heroku.com/)

Add to `build.sbt`

    enablePlugins(JavaAppPackaging)

Add to `project/plugin.sbt`

    addSbtPlugin("com.heroku" % "sbt-heroku" % "1.0.0")
    addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3")

## Dependency
Add to `build.sbt`

    lazy val akkaVersion = "2.4.2"
    libraryDependencies ++=
        Seq(
            "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
            "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
            "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
            "redis.clients" % "jedis" % "2.0.0"
        )

# Run local
    > sbt run
It should run on : [http://localhost:8188](http://localhost:8188)