package org.loopring.marketcap

import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.{ Slick, SlickSession }
import akka.stream.scaladsl.{ Sink, Source }
import slick.jdbc.{ GetResult, SQLActionBuilder }

import scala.concurrent.Future

class DatabaseAccesser(
  implicit
  mat: ActorMaterializer,
  session: SlickSession) {

  type ResultSet = slick.jdbc.PositionedResult
  type ResultInt = slick.dbio.DBIO[Int]

  def saveOrUpdate[T](params: T*)(implicit fallback: T ⇒ ResultInt): Future[Int] = {
    Source[T](params.to[collection.immutable.Seq]).via(Slick.flow(fallback)) runReduce (_ + _)
  }

  implicit class SQL(sql: SQLActionBuilder) {

    def list[T](implicit fallback: ResultSet ⇒ T): Future[Seq[T]] = {
      implicit val result: GetResult[T] = GetResult(fallback)
      Slick.source(sql.as[T]).runWith(Sink.seq)
    }

    def head[T](implicit fallback: ResultSet ⇒ T): Future[T] = {
      implicit val result: GetResult[T] = GetResult(fallback)
      Slick.source(sql.as[T]).runWith(Sink.head)
    }

  }

}
