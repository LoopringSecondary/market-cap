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

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.loopring.marketcap.cache.{ CacherSettings, ProtoBufMessageCacher }
import org.loopring.marketcap.proto.data.TokenVolume

import scala.concurrent.Future

object RedisUsage extends App {

  //  TokenVolume

  implicit val system = ActorSystem("RedisUsage", ConfigFactory.load())

  // 只能创建一次
  implicit val settings = CacherSettings(system.settings.config)

  implicit val ex = system.dispatcher

  // 每个Message创建一个 ProtoBufMessageCacher[T]
  val tokenVolumeCacher = new ProtoBufMessageCacher[TokenVolume]

  val qq: Future[Boolean] = tokenVolumeCacher.put("haha1", TokenVolume(10, "dududu1"))
  qq.onComplete {
    case scala.util.Success(value) ⇒ println(value)
    case scala.util.Failure(ex) ⇒ ex.printStackTrace()
  }


  // 先从redis获取数据, 找不到数据自动调用fallback函数, 并将结果保存到redis
  val dd = tokenVolumeCacher.getOrElse("haha2") {
    Future.successful(Some(TokenVolume(11, "dududu2")))
  }

  dd.onComplete {
    case scala.util.Success(Some(x)) ⇒ println(x)
    case scala.util.Failure(ex) ⇒ ex.printStackTrace()
  }

}
