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
