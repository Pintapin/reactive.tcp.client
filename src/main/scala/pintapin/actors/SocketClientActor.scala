package pintapin.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

object SocketClientActor {

  case class CloseConnection(String reason = "")
  def props() = ???
}

class SocketClientActor(remote: InetSocketAddress, tcpActor: ActorRef) extends Actor {
  val log = Logging(context.system, this)

  val manager = if (tcpActor == null) Tcp.get(getContext().system()).manager() else tcpActor

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
    case _ => unhandled()
  }

  def connected(self: ActorRef) = {
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