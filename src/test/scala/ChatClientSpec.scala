import akka.actor.{ActorSystem, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe
import akka.testkit.{ImplicitSender, DefaultTimeout, TestKit, TestProbe}
import chat.ChatClient
import chat.ChatHandler._
import events.Events
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._

/**
  * Created by yjtsai on 2016/3/16.
  */
class ChatClientSpec extends TestKit(ActorSystem("ActorSpec")) with DefaultTimeout with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {
  val topic = "topic"

  override def afterAll {
    shutdown()
  }

  "Mediator" should {
    "send Events.Joined to all subscribers" in {
      val mediator = DistributedPubSub(system).mediator
      val member1 = TestProbe()
      val member2 = TestProbe()
      mediator ! Subscribe(topic, member1.ref)
      mediator ! Subscribe(topic, member2.ref)

      val client = system.actorOf(Props(classOf[ChatClient], topic), "client")
      client ! Join("client", client)

      member1.expectMsgPF(3 seconds){case Events.Joined(sender, members) => {println(Events.Joined(sender, members));true}}
      member2.expectMsgPF(3 seconds){case Events.Joined(sender, members) => {println(Events.Joined(sender, members));true}}

    }

    "send Events.ChatMessage to all subscribers" in {
      val mediator = DistributedPubSub(system).mediator
      val member1 = TestProbe()
      val member2 = TestProbe()
      mediator ! Subscribe(topic, member1.ref)
      mediator ! Subscribe(topic, member2.ref)

      val client2 = system.actorOf(Props(classOf[ChatClient], topic), "client2")
      client2 ! ReceivedMessage("client2","hello")

      member1.expectMsgPF(3 seconds){case msg : Events.ChatMessage => {println(msg);true}}
      member2.expectMsgPF(3 seconds){case msg : Events.ChatMessage => {println(msg);true}}
    }

    "send Events.Leaved to all subscribers" in {
      val mediator = DistributedPubSub(system).mediator
      val member1 = TestProbe()
      val member2 = TestProbe()
      mediator ! Subscribe(topic, member1.ref)
      mediator ! Subscribe(topic, member2.ref)

      val client3 = system.actorOf(Props(classOf[ChatClient], topic), "client3")
      client3 ! Left("client3")

      member1.expectMsgPF(3 seconds){case msg : Events.ChatMessage => {println(msg);true}}
      member2.expectMsgPF(3 seconds){case msg : Events.ChatMessage => {println(msg);true}}
    }
  }
}

