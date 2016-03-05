package chat

import akka.actor.{Terminated, Status, ActorRef, Actor}
import akka.cluster.pubsub.{DistributedPubSubMediator, DistributedPubSub}
import akka.cluster.pubsub.DistributedPubSubMediator._
import chat.ChatHandler._
import events.Events
import redis.RedisService
/**
  * Created by Phyrex on 2016/3/3.
  */
class ChatClient(topic : String) extends Actor {


  val mediator = DistributedPubSub(context.system).mediator;

  var subscribers : (String, String, ActorRef) = null

  def receive = {
    case Join(name, subscriber) => {
      context.watch(subscriber)
      subscribers = (topic, name, subscriber)
      mediator ! Subscribe(topic, subscriber)

      RedisService.connection.set(s"$topic-$name", subscriber.toString)
      println("redis : " + RedisService.connection.get(topic + "-" + name))

      boardcast(Events.Joined(name, /*members*/null))
    }
    case msg : ReceivedMessage => {
      boardcast(msg.toChatMessage)
    }
    case msg : Events.ChatMessage => {
      boardcast(msg)
    }
    case Left(name) => {
      val entry @ (topic, username, ref) = subscribers
      ref ! Status.Success(Unit)
      mediator ! Unsubscribe(topic, self)
      //redis.del(s"$topic-$username")
      boardcast(Events.Leaved(name, /*members*/null))
    }
    case Terminated(subscriber) => {
      mediator ! Unsubscribe(topic, subscriber)
    }
  }

  def boardcast(msg: Events.Message) {
    mediator ! Publish(topic, msg)
  }
}


