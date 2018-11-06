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

import scalapb.json4s.{ Parser, Printer }
import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Timers }
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest }
import akka.util.Timeout
import akka.stream.ActorMaterializer
import org.loopring.marketcap.broker.HttpConnector
import org.loopring.marketcap.proto.data.{ MarketTickData, _ }
import org.loopring.marketcap.SignatureUtil
import scala.concurrent.Future
import akka.pattern.{ ask }
import scala.concurrent.duration._

class MarketTickerCrawlerActor(marketTickerServiceActor: ActorRef, tokenInfoServiceActor: ActorRef, implicit val system: ActorSystem, val mat: ActorMaterializer) extends Actor with HttpConnector with Timers with ActorLogging with SignatureUtil {

  implicit val timeout = Timeout(5 seconds)
  implicit val _mat = mat

  val appId = system.settings.config.getString("my_token.app_id")
  val connection = http(system.settings.config.getString("my_token.host_url"))
  val appSecret = system.settings.config.getString("my_token.app_secret")

  override def preStart(): Unit = {
    //daliy schedule market's ticker info
    timers.startSingleTimer("cronSyncMarketTicker", "syncMarketTicker", 30 seconds)
  }

  override def receive: Receive = {
    case s: String ⇒
      //load AllTokens
      val f = (tokenInfoServiceActor ? GetTokenListReq()).mapTo[GetTokenListRes]
      f.foreach {
        _.list.foreach { tokenInfo ⇒
          crawlMarketPairTicker(tokenInfo)
          Thread.sleep(50)
        }
      }
  }

  private def crawlMarketPairTicker(tokenInfo: TokenInfo): Unit = {
    var name_id = tokenInfo.source
    var symbol = tokenInfo.symbol
    var anchor = "eth"
    if (symbol == "ETH" || symbol == "WETH") {
      name_id = "ethereum"
      symbol = "eth"
      anchor = "usd"
    }
    val timestamp = System.currentTimeMillis() / 1000
    val sighTemp = "anchor=" + anchor + "&app_id=" + appId + "&name_id=" + name_id + "&symbol=" + symbol.toLowerCase() + "&timestamp=" + timestamp
    val signValue = bytesToHex(getHmacSHA256(appSecret, sighTemp + "&app_secret=" + appSecret)).toUpperCase()
    val uri = "/ticker/paironmarket?" + sighTemp + "&sign=" + signValue

    get(HttpRequest(uri = uri, method = HttpMethods.GET)) {
      case r if r.status.isSuccess() =>
        r.to[String].map {
          dataInfoStr =>
            val p = new Parser(preservingProtoFieldNames = true) //protobuf 序列化为json不使用驼峰命名
            val marketTickData = p.fromJsonString[MarketTickData](dataInfoStr)
            val lastUpdated = marketTickData.timestamp
            marketTickData.data.foreach {
              pairdata =>
                pairdata.marketList.foreach {
                  ticker =>
                    val symbol = ticker.symbol
                    val market = ticker.anchor
                    val exchange = ticker.marketName
                    val price = ticker.price.toDouble
                    val priceUsd = ticker.priceUsd.toDouble
                    val priceCny = ticker.priceCny.toDouble
                    val volume24hUsd = ticker.volume24HUsd.toDouble
                    val volume24hFrom = ticker.volume24HFrom.toDouble
                    val volume24h = ticker.volume24H.toDouble
                    val percentChangeUtc0 = ticker.percentChangeUtc0.toDouble
                    val alias = ticker.alias
                    val exchangeTickerInfo = ExchangeTickerInfo(symbol, market, exchange, price, priceUsd, priceCny,
                      volume24hUsd, volume24hFrom, volume24h, percentChangeUtc0, alias, lastUpdated)
                    marketTickerServiceActor ! exchangeTickerInfo
                }
            }
        }

      case _ =>
        log.error("get ticker data from my-token failed")
        Future.successful(ExchangeTickerInfo())
    }

  }

}

