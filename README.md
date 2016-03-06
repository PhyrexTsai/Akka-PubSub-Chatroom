# Akka-PubSub-Chatroom
Try Akka-PubSub-Chatroom : <br/>
<a href="https://heroku.com/deploy?template=https://github.com/PhyrexTsai/Akka-PubSub-Chatroom.git">
<img src="https://camo.githubusercontent.com/c0824806f5221ebb7d25e559568582dd39dd1170/68747470733a2f2f7777772e6865726f6b7563646e2e636f6d2f6465706c6f792f627574746f6e2e706e67" alt="Deploy" data-canonical-src="https://www.herokucdn.com/deploy/button.png" style="max-width:100%;">
</a>

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