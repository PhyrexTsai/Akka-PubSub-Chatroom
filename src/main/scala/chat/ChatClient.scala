package chat

import akka.actor.{Terminated, Status, ActorRef, Actor}
import akka.cluster.pubsub.{DistributedPubSubMediator, DistributedPubSub}
import akka.cluster.pubsub.DistributedPubSubMediator._
import chat.ChatHandler.{Left, ReceivedMessage, Join}
import events.Events

/**
  * Created by Phyrex on 2016/3/3.
  */
class ChatClient(topic : String) extends Actor {
  val mediator = DistributedPubSub(context.system).mediator;

  var subscribers = Set.empty[(String, String, ActorRef)]

  def members = subscribers.map(_._2).toSeq

  def receive = {
    case Join(name, subscriber) => {
      context.watch(subscriber)
      subscribers += ((topic, name, subscriber))
      mediator ! Subscribe(topic, subscriber)
      boardcast(Events.Joined(name, /*members*/null))
    }
    case msg : ReceivedMessage => {
      boardcast(msg.toChatMessage)
    }
    case msg: Events.ChatMessage => {
      boardcast(msg)
    }
    case Left(name) => {
      val entry @ (topic, username, ref) = subscribers.find(_._2 == name).get
      ref ! Status.Success(Unit)
      subscribers -= entry
      mediator ! Unsubscribe(topic, self)
      boardcast(Events.Leaved(name, /*members*/null))
    }
    case Terminated(subscriber) => {
      mediator ! Unsubscribe(topic, subscriber)
      subscribers = subscribers.filterNot(_._3 == subscriber)
    }
  }

  def boardcast(msg: Events.Message) {
    mediator ! Publish(topic, msg)
  }
}


