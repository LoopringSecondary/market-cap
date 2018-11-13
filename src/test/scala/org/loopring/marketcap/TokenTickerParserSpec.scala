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

import java.io.File

import org.scalatest.WordSpec
import org.json4s._
import org.json4s.native.JsonMethods._
import org.loopring.marketcap.proto.data._
import scalapb.json4s.JsonFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Await, Future }

import scala.concurrent.duration._

class TokenTickerParserSpec extends WordSpec {

  // 这里是模拟 http request 请求, 把response 中的 data 转换成 CMCTickerData
  // 这里是 USD 数据
  def getTokenUSD: Future[Seq[CMCTickerData]] =TokenTickerCrawlerActor.scala
    Future {
      (parse(new File("tokens_usd.json")) \\ "data")
        .children
        // TODO(michelle) 注意这里 : 这里采用驼峰是结构进行反序列化, 我这里便于测试, 只有 price 字段会反序列化回来, 其他字段都是0
        .map(JsonFormat.fromJson[CMCTickerData])
    }

  // 这里是 CNY 数据
  def getTokenCNY: Future[Seq[CMCTickerData]] =
    Future {
      (parse(new File("tokens_cny.json")) \\ "data")
        .children
        .map(JsonFormat.fromJson[CMCTickerData])
    }

  // 找到 "市场" 代币 的价格(usd)
  def findMarketUsd(tts: Seq[CMCTickerData]): Seq[(String, Double)] = {
    markets.map { s ⇒
      val priceOption = tts.find(_.symbol == s).flatMap(_.quote.get("USD").map(_.price))
      // TODO(michelle) 如果找不到 symbol 对应的价格 会报错
      require(priceOption.isDefined, s"can not found ${s} / USD")
      (s, priceOption.get)
    }
  }

  // 交易市场
  lazy val markets = Seq("LRC", "ETH", "TUSD", "USDT")


  def copyQuote(quote: Quote, price: Double): Quote = {
    // 根据 "市场/usd" 价格个计算 quote的 价格
    quote.copy(price = quote.price / price,
      volume24H = quote.volume24H / price,
      percentChange1H = quote.percentChange1H / price,
      percentChange24H = quote.percentChange24H / price,
      percentChange7D = quote.percentChange7D / price,
      marketCap = quote.marketCap / price)

  }

  // sbt "testOnly *TokenTickerParserSpec -- -z test1"
  "test1" in {

    val futureResult = for {
      usds ← getTokenUSD // 这里相当于从 http 获取数据
      markets = findMarketUsd(usds) // 这里找到 各 "市场" 相对美元价格
      // cnys ← getTokenCNY
    } yield {

      // 下面的循环对每个token进行处理
      // TODO(michelle) 这里的代码有 2094 个 可以 通过 filter 方法 过滤出来有用的吧?
      for {
        xusd ← usds

        quoteOption = xusd.quote.get("USD")   // 先找到每个token的 usd 价格

        _ = require(quoteOption.isDefined, s"can not found ${xusd.symbol} quote USD ")

        xquote = quoteOption.get

      } yield {

        // TODO(michelle) 这里的 foldLeft 是 以 原来的 quote 为基础 向左叠加
        val quoteMap = markets.foldLeft(xusd.quote) { (map, market) ⇒
          val (symbol, price) = market // 这里的 market 是个 tupl

          // 这里是向左叠, 在原理美元价格基础上 添加 ("LRC", "ETH", "TUSD", "USDT")
          map + (symbol → copyQuote(xquote, price))
        }

        // 更新属性
        xusd.copy(quote = quoteMap)
      }

    }

    val result = Await.result(futureResult, 10 seconds)

    // 输出前三个
    result.take(3).foreach { token ⇒
      println("=" * 50)
      println("=> " + token.symbol)
      println(token.quote)
    }

  }

}
