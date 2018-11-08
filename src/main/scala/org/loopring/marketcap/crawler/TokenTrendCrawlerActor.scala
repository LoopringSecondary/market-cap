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

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Timers }
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, HttpResponse, StatusCodes }
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.loopring.marketcap.SignatureUtil
import org.loopring.marketcap.broker.HttpConnector
import org.loopring.marketcap.cache.{ CacherSettings, ProtoBufMessageCacher }
import org.loopring.marketcap.proto.data.{ GetTokenListReq, GetTokenListRes, _ }
import scalapb.json4s.Parser

import scala.concurrent.Future
import scala.concurrent.duration._

class TokenTrendCrawlerActor(tokenInfoServiceActor: ActorRef)(
  implicit
  val system: ActorSystem,
  val mat: ActorMaterializer) extends Actor with HttpConnector with Timers with ActorLogging with SignatureUtil {

  implicit val timeout = Timeout(5 seconds)
  implicit val ec = context.system.dispatcher
  implicit val settings = CacherSettings(system.settings.config)

  val appId = system.settings.config.getString("my_token.app_id")
  val connection = http(system.settings.config.getString("my_token.host_url"))
  val appSecret = system.settings.config.getString("my_token.app_secret")
  val trendKey = "TOKEN_TREND_"

  val parser = new Parser(preservingProtoFieldNames = true) //protobuf 序列化为json不使用驼峰命名

  val cacher4TokenTrend = new ProtoBufMessageCacher[Trend]

  override def preStart(): Unit = {
    //daliy schedule market's ticker info
    timers.startPeriodicTimer("cronSyncTokenTrend", "syncTokenTrend", 1 hours)
  }

  override def receive: Receive = {
    case _: String ⇒
      //load AllTokens
      val f = (tokenInfoServiceActor ? GetTokenListReq()).mapTo[GetTokenListRes]
      f.foreach {
        _.list.foreach { tokenInfo ⇒
          crawlTokenTrendData(tokenInfo)
          // TODO(michelle) 这里的时间也做配置吧
          Thread.sleep(50)
        }
      }
  }

  private def crawlTokenTrendData(tokenInfo: TokenInfo): Unit = {
    // var name_id = tokenInfo.source
    val symbol = tokenInfo.symbol
    val name_id = if (symbol == "ETH" || symbol == "WETH") "ethereum" else tokenInfo.source


    // TODO(michelle) 下面这段常量有需要配置的吧
    // =========================== start
    val period = "1d"
    val timestamp = System.currentTimeMillis() / 1000
    //modify wait for my-token's new version ; trend_anchor="usd,cny,eth,btc",also require modify redis's struct
    val trend_anchor = "usd"
    val limit = 180
    // =========================== end

    val sighTemp = s"app_id=$appId&limit=$limit&name_id=$name_id&period=$period&timestamp=$timestamp&trend_anchor=$trend_anchor"
    val signValue = bytesToHex(getHmacSHA256(appSecret, s"$sighTemp&app_secret=$appSecret")).toUpperCase()
    val uri = s"/symbol/trend?$sighTemp&sign=$signValue"

    get(HttpRequest(uri = uri, method = HttpMethods.GET)) {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        entity.dataBytes.map(_.utf8String).runReduce(_ + _).map { dataInfoStr ⇒

          val dataInfo = parser.fromJsonString[TokenTrendData](dataInfoStr)
          dataInfo.data.foreach {
            trendData =>
              //set in redis cache
              cacher4TokenTrend.push(buildCacheKey(symbol, trend_anchor, period), trendData.trend)
            //println(Json(DefaultFormats).write(trendData.trend))
          }
        }

      case _ =>
        log.error("get trendmain data from my-token failed")
        Future.successful(Unit)
    }

  }

  def buildCacheKey(symbol: String, anchor: String, period: String) = {
    s"trendKey${symbol}_${anchor.toUpperCase()}_${period.toUpperCase()}"
  }

}

