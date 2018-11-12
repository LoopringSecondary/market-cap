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

import akka.actor._
import akka.stream.ActorMaterializer
import akka.pattern.pipe
import org.loopring.marketcap.proto.data._
import org.loopring.marketcap.cache._

class TokenTrendServiceActor(implicit
  system: ActorSystem,
  mat: ActorMaterializer) extends Actor {

  import system.dispatcher

  implicit val settings = CacherSettings(system.settings.config)

  val cacherTokenTrendData = new ProtoBufMessageCacher[Trend]
  val trendKey = "TOKEN_TREND_"

  override def receive: Receive = {

    case req: GetTokenTrendDataReq ⇒
      //这里只需查询缓存
      val res = cacherTokenTrendData.getSeq(buildCacheKey(req.symbol.getOrElse(""), req.period.getOrElse("")))

      res.map {
        case Some(r) => r
        case _ => throw new Exception("trend data in redis is null.")
      } pipeTo sender

  }

  def buildCacheKey(symbol: String, period: String) = {
    s"$trendKey${symbol.toUpperCase()}_${period.toUpperCase()}"
  }

}
