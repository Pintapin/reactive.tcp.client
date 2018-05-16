package pintapin

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}
import pintapin.actors.SocketClientActor

import scala.concurrent.ExecutionContext

object SocketClient extends App {

  implicit val config = ConfigFactory.defaultApplication()
  implicit val system = ActorSystem("tcp-client")
  implicit val executionContext = system.dispatcher

  val clientActor = system.actorOf(SocketClientActor.props())
}