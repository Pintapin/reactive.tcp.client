package snapptrip

import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.duration._

object Main extends App {

  val port = 2333
  val host = "0.0.0.0"

  implicit val system = ActorSystem()
  implicit val mater = ActorMaterializer()

  createTestServer()

  // Read via TCP socket with akka-stream
  Source
    .maybe[ByteString]
    .via(Tcp().outgoingConnection(host, port))
    .to(Sink.foreach(bs => println(bs.utf8String)))
    .run()

  /**
    * Create a test server that sends time, every second
    * @param host host (IP String)
    * @param port port (Int)
    */
  def createTestServer(host: String = host, port: Int = port) {
    val server = Tcp().bind(host, port)
    server.runForeach{ conn =>
      Source.tick(1.second, 1.second, LocalDateTime.now.toString)
        .map(f => ByteString(f))
        .via(conn.flow)
        .to(Sink.ignore).run
    }

  }
}

