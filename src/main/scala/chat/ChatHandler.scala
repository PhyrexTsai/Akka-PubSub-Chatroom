package chat

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.stream.{ClosedShape, FlowShape, OverflowStrategy}
import akka.stream.scaladsl.{GraphDSL, Source, Sink, Flow}
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
      // 練習將 chatFlow 使用 Flow.fromGraph 取代 Flow.fromSinkAndSource 進行改寫，Graph 可以更容易理解整個架構
      // reference : http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-graphs.html
      def chatFlow(topic : String, sender : String) : Flow[String, Events.Message, Any] = {
        val chatActor = system.actorOf(Props(classOf[ChatClient], topic))
        Flow.fromGraph(GraphDSL.create() { implicit b =>
          val sink = b.add(Flow[String]
            .map(ReceivedMessage(sender, _))  // 這邊的 wildcard type 為 String
            .to(Sink.actorRef[ChatEvent](chatActor, Left(sender))))

          val source = b.add(Source.actorRef[Events.Message](10, OverflowStrategy.fail) // 提高 buffer 的數量，可以避免發送大量訊息的時候容易死掉
            .mapMaterializedValue(chatActor ! Join(sender, _))) // 這邊的 wildcard type 為 ActorRef

          FlowShape(sink.in, source.out)
        })
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
