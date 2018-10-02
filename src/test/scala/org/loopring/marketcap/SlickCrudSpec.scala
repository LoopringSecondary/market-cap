package org.loopring.marketcap

import akka.NotUsed
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{ Flow, Source }
import org.loopring.marketcap.proto.data._
import slick.jdbc.{ PositionedResult, SQLActionBuilder }
import slick.sql.SqlStreamingAction

import scala.concurrent.Future

class SlickCrudSpec extends SlickSpec {


  val db = new DatabaseAccesser()

  "test1" must {
    "mysql query list or head" in {

      import db._
      import session.profile.api._

      // implicit val getUserResult = GetResult(r => GetTokenListRes(id = r.nextInt(), name = r.nextString(), symbol = r.nextString(), website = r.nextString()))


      //      implicit def getGetTokenListRes = (rs: PositionedResult) ⇒
      //        GetTokenListRes(id = rs.nextInt(), name = rs.nextString(), symbol = rs.nextString(), website = rs.nextString())
      //
      //
      //      // val ddd: DBIO[Seq[Int]] = () ⇒ sql"".as[Int]
      //
      //
      //      def dd(s: String): DBIO[Seq[Int]] = sql"".as[Int]
      //
      //      val qq: SQLActionBuilder = sql"""SELECT ID, NAME, symbol, website FROM t_token_list"""
      //
      //
      //      // val dd = qq andThen list[GetTokenListRes]
      //
      //
      //      val done = sql"""SELECT ID, NAME, symbol, website FROM t_token_list""".head[GetTokenListRes]
      //
      //      done.onComplete {
      //        case scala.util.Success(value) ⇒ println("value =>>" + value)
      //        case scala.util.Failure(exception) ⇒ exception.printStackTrace()
      //      }

      Thread.sleep(5000)

    }
  }

  "test3" must {
    "mysql insert " in {

      //      import session.profile.api._
      //
      //      val users = (1 to 10).map(i ⇒ GetTokenListRes(id = i, name = s"aa$i", symbol = s"bb$i", website = s"cc$i"))
      //
      //      implicit def insertUser(user: GetTokenListRes): slick.dbio.DBIO[Int] =
      //        sql"""INSERT INTO t_token_list(id, name, symbol, website)
      //        VALUES(${user.id}, ${user.name}, ${user.symbol}, ${user.website})""".asUpdate
      //
      //
      //      val qq: GetTokenListRes ⇒ Int = (ew: GetTokenListRes) ⇒ {
      //        ew.id
      //      }
      //
      //
      //      val d = (s: String) ⇒ s.length
      //
      //      val f: (String => Int) = s => s.length
      //
      //
      //
      //      //      val d: slick.dbio.DBIO[Int] = (user: GetTokenListRes) ⇒ {
      //      //        sql"""INSERT INTO t_token_list(id, name, symbol, website)
      //      //        VALUES(${user.id}, ${user.name}, ${user.symbol}, ${user.website})""".asUpdate
      //      //      }
      //
      //      val done = db.saveOrUpdate[GetTokenListRes](users: _*)
      //
      //
      //      done.onComplete {
      //        case scala.util.Success(value) ⇒ println("xxx =>>" + value)
      //        case scala.util.Failure(exception) ⇒ exception.printStackTrace()
      //      }

      Thread.sleep(5 * 1000)

    }
  }

}

case class TokenInfo(id: Int, name: String, symbol: String, website: String)
