package org.loopring.marketcap.broker

import akka.actor.{ Actor, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.stream.ActorMaterializer
import akka.pattern.pipe

import scala.concurrent.Future

class OkexMarketBroker(implicit sys: ActorSystem, mat: ActorMaterializer) extends Actor {

  import sys.dispatcher

  lazy val responseFuture: Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        uri = "https://www.okex.com/api/v1/kline.do?symbol=ltc_btc&type=1min"
      ), // settings = settings
    )

  override def receive: Receive = {
    case s: String ⇒
      val resp = for {
        x ← responseFuture
        y ← x.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
      } yield y

      resp pipeTo sender

  }
}
