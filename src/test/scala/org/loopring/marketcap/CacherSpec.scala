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
