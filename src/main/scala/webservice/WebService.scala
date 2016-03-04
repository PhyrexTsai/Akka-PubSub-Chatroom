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
  val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")
  TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
  // 這邊使用 system.dispather 來實作 implicit => ExcutionContextExcutor
  import system.dispatcher
  system.scheduler.schedule(Duration.create(60, SECONDS), Duration.create(60, SECONDS)) {
    chatroom.boardcastMessage(Events.ChatMessage(sender = "SystemTicker", s""))
  }

  def route = MainService.route ~ ChatService.route(chatroom) ~ getFromResourceDirectory("web")

}
