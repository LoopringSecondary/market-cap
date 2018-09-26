package org.loopring.marketcap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{ Message, WebSocketRequest }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Flow, Keep, Sink, Source }
import org.scalatest.FlatSpec

import scala.concurrent.Promise

class BinanceWebSocketApiSpec extends FlatSpec {

  implicit val sys = ActorSystem("binace")
  implicit val mat = ActorMaterializer()

  "test1" should "haha" in {

    val flow: Flow[Message, Message, Promise[Option[Message]]] =
      Flow.fromSinkAndSourceMat(
        Sink.foreach[Message](println),
        Source.maybe[Message]
      )(Keep.right)

    val (upgradeResponse, promise) =
      Http().singleWebSocketRequest(
        WebSocketRequest("wss://stream.binance.com:9443/ws/lrceth@aggTrade"),
        flow
      )

    promise.success(None)

    Thread.sleep(10 * 1000)

  }

}
