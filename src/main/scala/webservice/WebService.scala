package webservice

import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.concurrent.TimeUnit._

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import chat.ChatHandler
import events.Events
import route.{ChatService, MainService}

import scala.concurrent.duration.Duration

/**
  * Created by Phyrex on 2016/3/3.
  */
class WebService(implicit fm: Materializer, system: ActorSystem) extends Directives {
  val chatroom = ChatHandler.create(system)

  def route = MainService.route ~ ChatService.route(chatroom) ~ getFromResourceDirectory("web")

}
