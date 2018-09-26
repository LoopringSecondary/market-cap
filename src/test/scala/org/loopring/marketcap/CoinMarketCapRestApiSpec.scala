//package org.loopring.marketcap
//
//import akka.actor.ActorSystem
//import akka.http.scaladsl.model.headers.RawHeader
//import akka.stream.ActorMaterializer
//import org.scalatest.FlatSpec
//
//import scala.concurrent.Await
//import scala.concurrent.duration.Duration
//
//class CoinMarketCapRestApiSpec extends FlatSpec with HttpConnector {
//
//  implicit val sys = ActorSystem("binance")
//  implicit val mat = ActorMaterializer()
//
//  import sys.dispatcher
//
//  val h = RawHeader("X-CMC_PRO_API_KEY", "41d0609d-e207-43b9-885b-ce310031093d")
//
//  "latest" should "have size 0" in {
//
//    val resultFuture = for {
//      x ← get(
//        "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest",
//        scala.collection.immutable.Seq(h)
//      )
//      _ = println("===>>>" + x.status)
//      y ← x.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
//    } yield y
//
//    val res = Await.result(resultFuture, Duration.Inf)
//
//    info(res.toString)
//
//    sys.terminate()
//  }
//
//}
