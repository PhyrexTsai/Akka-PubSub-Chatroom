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

  "Mediator" should {
    "send Events.Joined to all subscribers" in {
      val mediator = DistributedPubSub(system).mediator
      val member1 = TestProbe()
      val member2 = TestProbe()
      mediator ! Subscribe(topic, member1.ref)
      mediator ! Subscribe(topic, member2.ref)

      val client = system.actorOf(Props(classOf[ChatClient], topic), "client")
      client ! Join("client", client)

      member1.expectMsgPF(3 seconds){case Events.Joined(sender, message) => {println(Events.Joined(sender, message));true}}
      member2.expectMsgPF(3 seconds){case Events.Joined(sender, message) => {println(Events.Joined(sender, message));true}}

    }
  }
}

