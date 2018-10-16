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

import akka.actor.{ Actor, ActorSystem, Timers }
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import org.loopring.marketcap.proto.data.BinanceTokenTicker

class BinanceMarketBroker(
  implicit
  val system: ActorSystem,
  val mat: ActorMaterializer)
  extends Actor with HttpConnector with Timers {

  override private[broker] val connection = https("api.binance.com", proxy = Some(true))

  override def preStart(): Unit = {
    // timers.startPeriodicTimer()
    // context.system.scheduler.schedule(2, 3, this, )
  }

  override def receive: Receive = {
    case s: String ⇒

      val done = get("/api/v1/ticker/24hr?symbol=LRCETH") {
        _.to[String]
      }

      done.onComplete {
        case scala.util.Success(json) ⇒ println(json)
        case scala.util.Failure(ex) ⇒ ex.printStackTrace()
      }

  }

}

