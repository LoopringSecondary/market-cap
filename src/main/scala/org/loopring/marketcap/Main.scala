package org.loopring.marketcap

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import slick.basic.DatabaseConfig
import slick.jdbc.{ GetResult, JdbcProfile }
import akka.stream.scaladsl._
import akka.stream.alpakka.slick.scaladsl._
import com.typesafe.config.ConfigFactory
import org.loopring.marketcap.proto.data.GetTokenListRes

import scala.concurrent.Future

object Main extends App {

  implicit val system = ActorSystem("Test", ConfigFactory.load())
  implicit val mat = ActorMaterializer()
  implicit val ec = system.dispatcher

  val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("slick-mysql", system.settings.config)

  implicit val session = SlickSession.forConfig(databaseConfig)


  implicit val getUserResult = GetResult(r => GetTokenListRes(id = r.nextInt(), name = r.nextString(), symbol = r.nextString(), website = r.nextString()))

  import session.profile.api._

  val done: Future[Seq[GetTokenListRes]] =
    Slick
      .source(sql"SELECT ID, NAME, symbol, website FROM t_token_list".as[GetTokenListRes])
      .log("user")
      .runWith(Sink.seq)

  done.map {
    _.map { x ⇒
      println("x  ===>>>" + x.id + "#" + x.symbol)
    }
  }


  system.registerOnTermination(() => session.close())
  // redis
  //  val nodePorts = Seq(7000, 7001, 7002)
  //
  //  implicit val cluster: RedisCluster = RedisCluster(nodePorts.map(p => RedisServer("192.168.0.200", p)))
  //
  //
  //  val req = GetTokenListRes(id = 10, name = "Loopring", symbol = "LRC", website = "loopring")
  //
  //
  //  val d = new ProtoBufMessageCacher[GetTokenListRes]()
  //
  //  d.put("haha", req).foreach(println)
  //
  //
  //  d.get("haha").foreach {
  //
  //    case Some(x) ⇒ println("xxx =>>>>" + x.name + "#" + x.symbol)
  //    case _ ⇒ println("nonononon")
  //
  //  }

}
