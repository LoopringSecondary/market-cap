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

import akka.actor.{ Actor, ActorSystem, Props }
import akka.routing.FromConfig
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

class MarketBrokerProxy(implicit sys: ActorSystem, mat: ActorMaterializer)
  extends Actor
  with LazyLogging {

  val binance =
    sys.actorOf(
      FromConfig.props(Props(new BinanceMarketBroker())),
      "binance_broker")

  override def receive: Receive = {
    case "binance" ⇒
      binance.forward("binance")
    case "okex" ⇒
      binance.forward("okex")
  }

}
