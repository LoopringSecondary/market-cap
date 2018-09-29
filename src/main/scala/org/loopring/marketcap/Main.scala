package org.loopring.marketcap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.util.{ Failure, Success }

object Main extends App {

  implicit val system = ActorSystem("Test", ConfigFactory.load())
  implicit val mat = ActorMaterializer()
  implicit val ec = system.dispatcher

  val root = new RootEndpoints

  val bind = Http().bindAndHandle(root(), interface = "0.0.0.0", port = 9000)

  bind.onComplete {
    case Success(value) ⇒
      println(s"Market-Cap Http/WebSocket Server started @ ${value.localAddress}")
    case Failure(ex) ⇒ ex.printStackTrace()
  }

  bind.failed.foreach(_.printStackTrace())

  //  val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("slick-mysql", system.settings.config)
  //
  //  implicit val session = SlickSession.forConfig(databaseConfig)
  //
  //
  //  system.registerOnTermination(() => session.close())

}
