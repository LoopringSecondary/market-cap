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

package org.loopring.marketcap.broker

import akka.actor.{Actor, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.pattern.pipe
import akka.stream.ActorMaterializer

import scala.concurrent.Future

class OkexMarketActor(implicit sys: ActorSystem, mat: ActorMaterializer) extends Actor {

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
