/*
 * Copyright 2018 Loopring Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package org.loopring.marketcap
//
//import akka.actor.ActorSystem
//import akka.stream.ActorMaterializer
//import org.scalatest.FlatSpec
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class BinanceRestApiSpec extends FlatSpec with HttpConnector {
//
//  implicit val sys = ActorSystem("binance")
//  implicit val mat = ActorMaterializer()
//
//  "kline" should "have size 0" in {
//
////    val resultFuture = for {
////      x ← get(
////        "https://api.binance.com/api/v1/klines?symbol=LRCUSDT&interval=3m"
////      )
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
//
//}
