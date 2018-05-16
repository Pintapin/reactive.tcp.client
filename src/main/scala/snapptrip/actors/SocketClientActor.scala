package snapptrip.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import akka.io.{IO, Tcp, TcpMessage}
import akka.util.ByteString
import java.net.InetSocketAddress

import akka.io.Tcp.{CommandFailed, Connected, ConnectionClosed, Received}
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext

// Builds the actor
object SocketClientActor {

  case class CloseConnection(reason: String = "")
  def props()(implicit ec: ExecutionContext, system: ActorSystem, config: Config) = {
    Props(new SocketClientActor(null, null)) //TODO
  }
}

// Actor to send and receive via tcp socket
class SocketClientActor(remote: InetSocketAddress, tcpActor: ActorRef) extends Actor {
  val log = Logging(context.system, this)

  //FIXME change to Option[ActorRef]
  val manager = if (tcpActor == null) Tcp.get(context.system).manager else tcpActor

  manager.tell(TcpMessage.connect(remote), self)

  override def receive = {
    case failed: CommandFailed => {
      log.info("cannot connect")
      context.stop(self)
    }
    case msg: Connected => {
      log.info("received connected message")
      sender ! TcpMessage.register(self)
      context.become(connected(self))
    }
    case _ => unhandled("Unhandled message")
  }

  /* Handles received messages when actor is connected
   *
   */
  def connected(self: ActorRef): Receive = {
    case msg: ByteString => {}
    case failed: CommandFailed => {}
    case msg: Received => {}
    case close: SocketClientActor.CloseConnection => {
      manager ! TcpMessage.close
    }
    case closed: ConnectionClosed => {
      context.stop(self)
    }
  }
}
