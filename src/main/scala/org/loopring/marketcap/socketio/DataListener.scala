package org.loopring.marketcap.socketio

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.corundumstudio.socketio.{ AckRequest, SocketIOClient }
import com.fasterxml.jackson.databind.ObjectMapper

import scala.concurrent.Await
import scala.concurrent.duration._

private[socketio] class DataListener[I, O](event: String, replyTo: ActorRef)(
  implicit
  serializr: String ⇒ I,
  deserializr: O ⇒ java.util.Map[String, Any])
  extends com.corundumstudio.socketio.listener.DataListener[Any] {

  implicit val timeout = Timeout(3 seconds)

  override def onData(client: SocketIOClient, data: Any, ackSender: AckRequest): Unit = {

    import scala.collection.JavaConverters._

    val headersJava = client.getHandshakeData.getHttpHeaders
    val headersMap = headersJava.asScala.foldLeft(Map.empty[String, String]) { (all, x) ⇒
      all + (x.getKey → x.getValue)
    }

    val json = new ObjectMapper().writeValueAsString(data)

    val inMessage = SocketInMessage[I](event, serializr(json), headersMap)

    val messageFuture = (replyTo ? inMessage).mapTo[SocketOutMessage]

    val message = Await.result(messageFuture, Duration.Inf)

    // TODO(Toan) message
    message match {
      case b: BroadcastMessage[O] ⇒
        val map = deserializr(b.t)
        client.getNamespace.getBroadcastOperations.sendEvent(b.event, map)
      case e: EventMessage[O] ⇒
        val map = deserializr(e.t)
        client.sendEvent(e.event, map)
      case r: ReplayMessage[O] ⇒
        val map = deserializr(r.t)
        ackSender.sendAckData(map)
      case _ ⇒ // logger.info("no message")
    }
  }

}
