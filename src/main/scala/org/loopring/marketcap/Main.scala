package org.loopring.marketcap

import akka.actor.{ ActorSystem, Props }
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.SlickSession
import com.typesafe.config.ConfigFactory
import org.loopring.marketcap.endpoints.RootEndpoints
import org.loopring.marketcap.tokens.TokenInfoServiceActor
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.util.{ Failure, Success }

object Main extends App {

  // for system
  implicit val system = ActorSystem("Test", ConfigFactory.load())
  implicit val mat = ActorMaterializer()
  implicit val ec = system.dispatcher

  // for db
  val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("slick-mysql", system.settings.config)
  implicit val session = SlickSession.forConfig(databaseConfig)
  system.registerOnTermination(() => session.close())


  // for endpoints
  val tokenInfoDatabaseActor = system.actorOf(Props(new TokenInfoServiceActor()), "token_info")
  val root = new RootEndpoints(tokenInfoDatabaseActor)
  val bind = Http().bindAndHandle(root(), interface = "0.0.0.0", port = 9000)
  bind.onComplete {
    case Success(value) ⇒
      println(s"Market-Cap Http/WebSocket Server started @ ${value.localAddress}")
    case Failure(ex) ⇒ ex.printStackTrace()
  }
  bind.failed.foreach(_.printStackTrace())



}
