package org.loopring.marketcap

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.{ClientTransport, Http}
import akka.http.scaladsl.model.{HttpHeader, HttpRequest, HttpResponse}
import akka.http.scaladsl.settings.{ClientConnectionSettings, ConnectionPoolSettings}
import akka.stream.ActorMaterializer
import org.scalatest.{FlatSpec, FunSpec}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class CoinMarketCapRestApiSpec extends FlatSpec {

  implicit val sys = ActorSystem("binance")
  implicit val mat = ActorMaterializer()

  import sys.dispatcher

//  val proxyHost = "127.0.0.1"
//  val proxyPort = 1087
//
//  lazy val httpsProxyTransport = ClientTransport.httpsProxy(
//    InetSocketAddress.createUnresolved(proxyHost, proxyPort)
//  )
//
//  lazy val settings = ConnectionPoolSettings(sys)
//    .withConnectionSettings(
//      ClientConnectionSettings(sys)
//        .withTransport(httpsProxyTransport)
//    )

  val h = RawHeader("X-CMC_PRO_API_KEY", "41d0609d-e207-43b9-885b-ce310031093d")


  lazy val responseFuture: Future[HttpResponse] =
    Http().singleRequest(
      HttpRequest(
        uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest",
         headers = scala.collection.immutable.Seq(h)
      ), // settings = settings
    )

  "latest" should "have size 0" in {

    val resultFuture = for {
      x ← responseFuture
      _ = println("===>>>" + x.status)
      y ← x.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
    } yield y

    val res = Await.result(resultFuture, Duration.Inf)

    info(res.toString)

    sys.terminate()
  }

}
