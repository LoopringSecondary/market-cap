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

import akka.actor.{ ActorSystem, Props }
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.typesafe.config.ConfigFactory
import org.loopring.marketcap.crawler._
import org.loopring.marketcap.endpoints.RootEndpoints
import org.loopring.marketcap.proto.data._
import org.loopring.marketcap.tokens.TokenInfoServiceActor
import slick.basic.DatabaseConfig
import akka.pattern.{ AskTimeoutException, ask }
import slick.jdbc.JdbcProfile
import akka.util.Timeout

import scala.util.{ Failure, Success }
import scala.concurrent.duration._

object Main extends App {

  // for system
  implicit val system = ActorSystem("Test", ConfigFactory.load())
  implicit val mat = ActorMaterializer()
  implicit val ec = system.dispatcher
  implicit val timeout = Timeout(5 seconds)

  //for db
  val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("slick-mysql", system.settings.config)
  implicit val session = SlickSession.forConfig(databaseConfig)
  system.registerOnTermination(() => session.close())

  //query tokenlist
  val tokenInfoDatabaseActor = system.actorOf(Props(new TokenInfoServiceActor()), "token_info")

  //crawl token's icoInfo
  //val tokenIcoCrawlerActor = system.actorOf(Props(new TokenIcoCrawlerActor(tokenIcoServiceActor, tokenInfoDatabaseActor)), "token_ico_crawler")
  /*val tokenIcoServiceActor = system.actorOf(Props(new TokenIcoServiceActor()), "token_ico_service")
  val f = (tokenIcoServiceActor ? GetTokenIcoInfoReq()).mapTo[GetTokenIcoInfoRes]
  f.foreach {
    _.list.foreach {
      ico =>
        println("test ico start")
        println(ico.tokenAddress)
        println(ico.icoStartDate)
        println("test ico end")
    }
  }*/

  //crawl market Ticker
  //val marketTickerCrawlerActor = system.actorOf(Props(new MarketTickerCrawlerActor(marketTickerServiceActor, tokenInfoDatabaseActor)), "market-ticker")
  //val marketTickerServiceActor = system.actorOf(Props(new MarketTickerServiceActor()), "market-ticker-service")
  /*val f = (marketTickerServiceActor ? GetExchangeTickerInfoReq(Some("lrc"), Some("eth"))).mapTo[GetExchangeTickerInfoRes]
  f.foreach {
    _.list.foreach {
      market =>
        println(market.exchange)
    }
  }*/

  //val tokenTrendCrawlerActor = system.actorOf(Props(new TokenTrendCrawlerActor(tokenInfoDatabaseActor)), "token_trend_crawler")
  /*val tokenTrendServiceActor = system.actorOf(Props(new TokenTrendServiceActor()), "token_trend_service")
  val f = (tokenTrendServiceActor ? GetTokenTrendDataReq(Some("LRC"), Some("1d"))).mapTo[Seq[Trend]]
  f.foreach {
    _.foreach(trend => println(trend.time + "," + trend.price + "," + trend.volumeTo))
  }*/

  //val tokenTickerCrawlerActor = system.actorOf(Props(new TokenTickerCrawlerActor(tokenTickerServiceActor)), "token_ticker_crawler")
  //val tokenTickerServiceActor = system.actorOf(Props(new TokenTickerServiceActor()), "token_ticker_service")
  /*val f = (tokenTickerServiceActor ? GetTokenTickerInfoReq(Some("ETH"))).mapTo[GetTokenTickerInfoRes]
    f.foreach {
    ticker => println(ticker.list.size)
  }*/

  // for endpoints
  val root: RootEndpoints = new RootEndpoints(tokenInfoDatabaseActor)
  val bind = Http().bindAndHandle(root(), interface = "0.0.0.0", port = 9000)

  bind.onComplete {
    case Success(value) ⇒
      println(s"Market-Cap Http/WebSocket Server started @ ${value.localAddress}")
    case Failure(ex) ⇒ ex.printStackTrace()
  }
  bind.failed.foreach(_.printStackTrace())

}
