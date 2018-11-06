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

package org.loopring.marketcap

import java.text.SimpleDateFormat

import akka.actor.{ ActorSystem, Props }
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.typesafe.config.ConfigFactory
import org.loopring.marketcap.broker.BinanceMarketBroker
import org.loopring.marketcap.endpoints.RootEndpoints
import org.loopring.marketcap.socketio.SocketIOServer
import org.loopring.marketcap.tokens.TokenInfoServiceActor
import org.loopring.marketcap.crawler.{ MarketTickerCrawlerActor, MarketTickerServiceActor, _ }
import org.loopring.marketcap.proto.data.TokenTickerInfo

import scala.collection.mutable
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.util.{ Failure, Success }

object Main extends App {

  // for system
  implicit val system = ActorSystem("Test", ConfigFactory.load())
  implicit val mat = ActorMaterializer()
  implicit val ec = system.dispatcher
  //val binance = system.actorOf(Props(new BinanceMarketBroker()))
  //binance ! ""

  //for db
  val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("slick-mysql", system.settings.config)
  implicit val session = SlickSession.forConfig(databaseConfig)
  system.registerOnTermination(() => session.close())

  //crawler token's icoInfo
  //val tokenIcoServiceActor = system.actorOf(Props(new TokenIcoServiceActor()), "token_ico_service")
  //val tokenIcoCrawlerActor = system.actorOf(Props(new TokenIcoCrawlerActor(tokenIcoServiceActor, tokenInfoDatabaseActor)), "token_ico_crawler")

  //query tokenlist
  val tokenInfoDatabaseActor = system.actorOf(Props(new TokenInfoServiceActor()), "token_info")
  //query tokenIcolist
  //val tokenIcoCrawlerActor = system.actorOf(Props(new TokenIcoCrawlerActor(tokenIcoServiceActor, tokenInfoDatabaseActor)), "token_ico_crawler")

  //val tokenTickerServiceActor = system.actorOf(Props(new TokenTickerServiceActor()), "token_ticker_service")
  //val tokenTickerCrawlerActor = system.actorOf(Props(new TokenTickerCrawlerActor(tokenTickerServiceActor, system, mat)), "token_ticker_crawler")
  val marketTickerServiceActor = system.actorOf(Props(new MarketTickerServiceActor()), "market-ticker-service")
  val marketTickerActor = system.actorOf(Props(new MarketTickerCrawlerActor(marketTickerServiceActor, tokenInfoDatabaseActor, system, mat)), "market-ticker")

  // for endpoints
  val root: RootEndpoints = new RootEndpoints(tokenInfoDatabaseActor)
  val bind = Http().bindAndHandle(root(), interface = "0.0.0.0", port = 9000)

  bind.onComplete {
    case Success(value) ⇒
      println(s"Market-Cap Http/WebSocket Server started @ ${value.localAddress}")
    case Failure(ex) ⇒ ex.printStackTrace()
  }
  bind.failed.foreach(_.printStackTrace())

  new SocketIOServer

}
