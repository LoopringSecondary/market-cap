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
import org.loopring.marketcap.proto.data._
import org.loopring.marketcap.SeqTpro
import org.loopring.marketcap.DatabaseAccesser

class TokenTickerServiceActor(implicit
  system: ActorSystem,
  mat: ActorMaterializer,
  session: SlickSession) extends DatabaseAccesser with Actor {

  import session.profile.api._

  implicit val saveTokenTickerInfo = (info: TokenTickerInfo) ⇒
    sqlu"""INSERT INTO t_token_ticker_info(token_id, token_name,
          symbol, website_slug, market, cmc_rank, circulating_supply, total_supply,
              max_supply,price,volume_24h,market_cap,percent_change_1h,percent_change_24h,percent_change_7d,last_updated) VALUES(
          ${info.tokenId}, ${info.name}, ${info.symbol}, ${info.websiteSlug},
          ${info.market}, ${info.rank}, ${info.circulatingSupply}, ${info.totalSupply}, ${info.maxSupply},${info.price},${info.volume24H},
          ${info.marketCap},${info.percentChange1H},${info.percentChange24H},${info.percentChange7D},${info.lastUpdated})"""

  override def receive: Receive = {
    case info: TokenTickerInfo ⇒

      saveOrUpdate(info)

    case info: SeqTpro[_] ⇒

      saveOrUpdate(info.t.map(_.asInstanceOf[TokenTickerInfo]): _*)
  }

}

