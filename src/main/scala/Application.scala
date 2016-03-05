import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import webservice.WebService

import scala.util.{Failure, Success}

/**
  * Created by Phyrex on 2016/3/3.
  */
object Application extends App {

  implicit val system = ActorSystem("Akka-PubSub-Chatroom")
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  val clusterAddress = Cluster(system).selfAddress
  Cluster(system).join(clusterAddress)

  val config = system.settings.config
  val interface = config.getString("app.host")
  val port = config.getInt("app.port")

  val service = new WebService

  val binding = Http().bindAndHandle(service.route, interface, port)
  binding.onComplete {
    case Success(binding) =>
      val localAddress = binding.localAddress
      println(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    case Failure(e) =>
      println(s"Binding failed with ${e.getMessage}")
      system.shutdown()
  }
}