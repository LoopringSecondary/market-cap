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

import java.text.SimpleDateFormat

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Timers }
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, HttpResponse, StatusCodes }
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.loopring.marketcap.SeqTpro
import org.loopring.marketcap.broker.HttpConnector
import org.loopring.marketcap.proto.data._
import scalapb.json4s.Parser

import scala.concurrent.Future
import scala.concurrent.duration._

class TokenTickerCrawlerActor(tokenTickerServiceActor: ActorRef)(
  implicit
  val system: ActorSystem,
  val mat: ActorMaterializer) extends Actor with HttpConnector with Timers with ActorLogging {

  implicit val timeout = Timeout(5 seconds)
  implicit val ec = context.system.dispatcher

  val config = system.settings.config

  val connection = https(config.getString("cmc-config.prefix_url"))
  val limitSize = config.getString("cmc-config.limitSize").toInt
  val pageCount = config.getString("cmc-config.pageCount").toInt
  val appKey = config.getString("cmc-config.api_key")

  val rawHeader = RawHeader(config.getString("cmc-config.header"), appKey)
  val convertCurrency = Seq("CNY", "USD", "ETH", "LRC", "USDT", "TUSD")

  val parser = new Parser(preservingProtoFieldNames = true) //protobuf 序列化为json不使用驼峰命名

  override def preStart(): Unit = {
    //daliy schedule token's ticker info
    timers.startPeriodicTimer("cronSyncTokenTicker", "syncTokenTicker", 600 seconds)
  }

  override def receive: Receive = {
    case _: String ⇒
      convertCurrency.foreach {
        currency =>
          getReq(currency)
          Thread.sleep(50)
      }

  }

  def getReq(currency: String): Unit = {
    //max support limitSize=5000, now set pageCount = 1
    (0 until pageCount).map { i =>

      val uri = s"/v1/cryptocurrency/listings/latest?start=${i * limitSize + 1}&limit=${limitSize}&convert=${currency}"

      get(HttpRequest(uri = uri, method = HttpMethods.GET).withHeaders(rawHeader)) {
        case HttpResponse(StatusCodes.OK, _, entity, _) =>

          entity.dataBytes.map(_.utf8String).runReduce(_ + _).map { dataInfoStr ⇒
            val dataInfo = parser.fromJsonString[TickerDataInfo](dataInfoStr)
            val fixGroup = dataInfo.data.grouped(100)
            fixGroup.foreach {
              group =>
                tokenTickerServiceActor ! convertTO(group, currency)
            }
          }

        case _ =>
          log.error("get ticker data from coinmarketcap failed")
          Future.successful(Unit)
      }
    }
  }

  def convertTO(tickers: Seq[CMCTickerData], currency: String): SeqTpro[TokenTickerInfo] = {

    val f = tickers.foldLeft(Seq.empty[TokenTickerInfo]) { (tickerinfos, ticker) ⇒

      val id = ticker.id
      val name = ticker.name
      val symbol = ticker.symbol
      val websiteSlug = ticker.slug
      val rank = ticker.cmcRank
      val circulatingSupply = ticker.circulatingSupply
      val totalSupply = ticker.totalSupply
      val maxSupply = ticker.maxSupply
      val market = currency
      val quote = ticker.quote.apply(currency)
      val price = quote.price
      val volume24h = quote.volume24H
      val marketCap = quote.marketCap
      val percentChange1h = quote.percentChange1H
      val percentChange24h = quote.percentChange24H
      val percentChange7d = quote.percentChange7D
      val utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z")
      val lastUpdated = utcFormat.parse(quote.lastUpdated.replace("Z", " UTC")).getTime / 1000
      val tickerInfo = TokenTickerInfo(id, name, symbol, websiteSlug, market, rank, circulatingSupply, totalSupply, maxSupply,
        price, volume24h, marketCap, percentChange1h, percentChange24h, percentChange7d, lastUpdated)

      tickerinfos :+ tickerInfo
    }

    SeqTpro(f)
  }

}
