package org.loopring.marketcap.broker

import java.net.InetSocketAddress

import akka.actor.{ Actor, ActorSystem }
import akka.http.scaladsl.{ ClientTransport, Http }
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.http.scaladsl.settings.{
  ClientConnectionSettings,
  ConnectionPoolSettings
}
import akka.stream.ActorMaterializer
import akka.pattern.pipe

import scala.concurrent.Future

class BinanceMarketBroker(implicit sys: ActorSystem, mat: ActorMaterializer)
  extends Actor {

  import sys.dispatcher

  val proxyHost = "127.0.0.1"
  val proxyPort = 1087

  val httpsProxyTransport = ClientTransport.httpsProxy(
    InetSocketAddress.createUnresolved(proxyHost, proxyPort))

  val settings = ConnectionPoolSettings(sys)
    .withConnectionSettings(
      ClientConnectionSettings(sys)
        .withTransport(httpsProxyTransport))

  lazy val responseFuture: Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        uri = "https://api.binance.com/api/v1/klines?symbol=LRCETH&interval=3m"),
      settings = settings)

  override def receive: Receive = {
    case s: String ⇒
      val resp = for {
        x ← responseFuture
        y ← x.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
      } yield y

      resp pipeTo sender
  }
}
