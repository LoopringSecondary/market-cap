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

import org.loopring.marketcap.proto.data._
import redis.{ RedisCluster, RedisServer }

import scala.util.Random

class CacherSpec extends MarketCapSpec {

  val nodePorts = Seq(7000, 7001, 7002)

  implicit val redis: RedisCluster = RedisCluster(nodePorts.map(p => RedisServer("192.168.0.200", p)))

  "test1" must {
    "redis set" in {

      //      val req = GetTokenListRes(id = 10, name = "Loopring", symbol = "LRC", website = "loopring")
      //      val d = new ProtoBufMessageCacher[GetTokenListRes]()
      //      d.put("haha", req).foreach(println)
      //
      //      d.get("haha").foreach {
      //        case Some(x) ⇒ println("xxx =>>>>" + x.name + "#" + x.symbol)
      //        case _ ⇒ println("nonononon")
      //      }
    }
  }

  "test2" must {

    "redis set with timeout" in {

      //      val req = GetTokenListRes(id = 10, name = "Loopring", symbol = "LRC", website = "loopring")
      //      val d = new ProtoBufMessageCacher[GetTokenListRes]()
      //
      //
      //      d.put("ddk", req, 10l).foreach(println)
      //
      //      // Thread.sleep(10)
      //
      //      //      redis.lpush()
      //
      //      d.get("ddhk").foreach {
      //        case Some(x) ⇒ println("eeee =>>>>" + x.name + "#" + x.symbol)
      //        case _ ⇒ println("nonononon")
      //      }
    }

  }

  "test3" must {
    "redis lpush" in {

      //      val req = GetTokenListRes(id = Random.nextInt(100), name = "Loopring", symbol = "LRC", website = "loopring")
      //
      //
      //      val d = new ProtoBufMessageCacher[GetTokenListRes]()
      //
      //
      //      d.push("haha", Seq.fill(10)(req)).foreach(println)

    }
  }

  "test4" must {
    "redis lrage" in {

      //      val d = new ProtoBufMessageCacher[GetTokenListRes]()
      //
      //      d.pull("haha", 0, -1).foreach {
      //        _.foreach { x ⇒
      //          println("xx =>>" + x.id)
      //
      //        }
      //      }

    }
  }

}
