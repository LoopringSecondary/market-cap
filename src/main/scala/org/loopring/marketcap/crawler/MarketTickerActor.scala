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
import akka.http.scaladsl.model.headers.{ ModeledCustomHeader, ModeledCustomHeaderCompanion }
import akka.stream.ActorMaterializer
import org.loopring.marketcap.broker.HttpConnector
import org.loopring.marketcap.proto.data._
import org.loopring.marketcap.SeqTpro
import org.loopring.marketcap.SignatureUtil
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try

class MarketTickerActor(implicit val system: ActorSystem, val mat: ActorMaterializer) extends Actor with HttpConnector with Timers with ActorLogging with SignatureUtil {

  implicit val timeout = Timeout(5 seconds)

  val appId = system.settings.config.getString("my_token.app_id")
  val connection = http(system.settings.config.getString("my_token.host_url"))
  val appSecret = system.settings.config.getString("my_token.app_secret")

  override def preStart(): Unit = {
    //daliy schedule market's ticker info
    timers.startSingleTimer("cronSyncMarketTicker", "syncMarketTicker", 30 seconds)
  }

  override def receive: Receive = {
    case s: String ⇒
      val timestamp = System.currentTimeMillis() / 1000
      val symbol = "lrc";
      val name_id = "loopring"
      val anchor = "eth"
      val sighTemp = "anchor=" + anchor + "&app_id=" + appId + "&name_id=" + name_id + "&symbol=" + symbol + "&timestamp=" + timestamp
      val signValue = bytesToHex(getHmacSHA256(appSecret, sighTemp + "&app_secret=" + appSecret)).toUpperCase()

      println(signValue)

      val uri = "http://openapi.mytokenapi.com/ticker/paironmarket?" + sighTemp + "&sign=" + signValue
      println(uri)
      val done = get(uri) {
        _.to[String]
      }
      done.onComplete {
        case scala.util.Success(json) ⇒ println(json)
        case scala.util.Failure(ex) ⇒ ex.printStackTrace()
      }
  }
}

