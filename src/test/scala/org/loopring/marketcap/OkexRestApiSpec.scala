//package org.loopring.marketcap
//
//import akka.actor.ActorSystem
//import akka.stream.ActorMaterializer
//import org.scalatest.FlatSpec
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class OkexRestApiSpec extends FlatSpec with HttpConnector {
//
//  implicit val sys = ActorSystem("binance")
//  implicit val mat = ActorMaterializer()
//
//  import sys.dispatcher
//
//  "kline" should "have size 0" in {
//
////    val resultFuture = for {
////      x ← get("https://www.okex.com/api/v1/kline.do?symbol=ltc_btc&type=1min")
////
////      _ = println("===>>>" + x.status)
////
////      y ← x.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
////    } yield y
////
////    val res = Await.result(resultFuture, Duration.Inf)
////
////    info(res.toString)
////
////    sys.terminate()
//  }
//}
