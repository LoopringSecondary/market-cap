package org.loopring.marketcap

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpec }
import akka.stream.scaladsl._
import akka.stream.alpakka.slick.scaladsl._
import akka.testkit.TestKit
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class SlickSpec extends WordSpec
  with ScalaFutures
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MustMatchers {

  implicit val system = ActorSystem("SlickSpec", ConfigFactory.load())
  implicit val mat = ActorMaterializer()
  implicit val ec = system.dispatcher

  val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("slick-mysql")
  implicit val session = SlickSession.forConfig(databaseConfig)

  import session.profile.api._


  override def afterAll(): Unit = {
    //#close-session
    system.registerOnTermination(() => session.close())
    //#close-session

    TestKit.shutdownActorSystem(system)
  }

}
