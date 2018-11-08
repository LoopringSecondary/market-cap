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

package org.loopring.marketcap.crawler

import akka.actor.{ Actor, ActorSystem }
import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.SlickSession
import org.loopring.marketcap.DatabaseAccesser
import org.loopring.marketcap.proto.data.{ ExchangeTickerInfo }

class MarketTickerServiceActor(
  implicit
  system: ActorSystem,
  mat: ActorMaterializer,
  session: SlickSession) extends DatabaseAccesser with Actor {

  import session.profile.api._

  override def receive: Receive = {
    case info: ExchangeTickerInfo ⇒

      implicit val saveExchangeTickerInfo = (info: ExchangeTickerInfo) ⇒
        sqlu"""INSERT INTO t_exchange_ticker_info(symbol, market,
          exchange, price, price_usd, price_cny, volume_24h_usd, volume_24h,volume_24h_from,percent_change_utc0,alias,last_updated) VALUES(
          ${info.symbol}, ${info.market}, ${info.exchange}, ${info.price},
          ${info.priceUsd}, ${info.priceCny}, ${info.volume24HUsd}, ${info.volume24H},${info.volume24HFrom},${info.percentChangeUtc0},${info.alias},${info.lastUpdated}) ON DUPLICATE KEY UPDATE price=${info.price},
          price_usd=${info.priceUsd},price_cny=${info.priceCny},volume_24h_usd=${info.volume24HUsd},volume_24h=${info.volume24H},
          volume_24h_from=${info.volume24HFrom},percent_change_utc0=${info.percentChangeUtc0},alias=${info.alias},last_updated=${info.lastUpdated}"""

      saveOrUpdate(info)
  }

}
