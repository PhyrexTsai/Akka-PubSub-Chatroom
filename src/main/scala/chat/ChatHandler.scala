package chat

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Source, Sink, Flow}
import events.Events

/**
  * Created by Phyrex on 2016/3/3.
  */
trait ChatHandler {
  def chatFlow(topic : String, sender : String) : Flow[String, Events.Message, Any]
}

object ChatHandler {
  def create(system : ActorSystem): ChatHandler = {
    new ChatHandler {
      // Flow => 1 input, 1 output   Source => 0 input, 1 output    Sink => 1 input, 0 output
      // 這邊使用 chatFlow 來表示使用者收到訊息、登入與登出的整個串流
      def chatFlow(topic : String, sender : String) : Flow[String, Events.Message, Any] = {
        val chatActor = system.actorOf(Props(classOf[ChatClient], topic))

        def remove = {
          Left(sender)
        }

        val in = Flow[String]
          .map((msg) => {
            ReceivedMessage(sender, msg)
          })
          .to(Sink.actorRef[ChatEvent](chatActor, remove))

        val out = Source.actorRef[Events.Message](10, OverflowStrategy.fail)
          .mapMaterializedValue((ref) => {
            chatActor ! Join(sender, ref)
          })

        Flow.fromSinkAndSource(in, out)
      }
    }
  }

  sealed trait ChatEvent
  case class Join(name : String, subscriber : ActorRef) extends ChatEvent
  case class Left(name : String) extends ChatEvent
  case class ReceivedMessage(sender : String, message : String) extends ChatEvent {
    def toChatMessage : Events.ChatMessage = Events.ChatMessage(sender, message)
  }
}
