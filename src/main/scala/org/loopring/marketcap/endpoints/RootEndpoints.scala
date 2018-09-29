package org.loopring.marketcap.endpoints

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import org.loopring.marketcap.Json4sSupport
import org.loopring.marketcap.proto.data._

import scala.concurrent.duration._

class RootEndpoints(
  tokenInfoDatabaseActor: ActorRef)(
  implicit
  system: ActorSystem) extends Json4sSupport {

  import system.dispatcher

  implicit val timeout = Timeout(5 seconds)

  val webSocketEndpoints = new WebSocketEndpoints

  def apply(): Route = {
    root ~ webSocketEndpoints()
  }

  def root: Route = {
    pathEndOrSingleSlash {
      get {
        complete("hello")
      }
    }
  } ~ path("tokens") {
    get {
      // TODO(Toan) 这里应该有查询条件
      val f = (tokenInfoDatabaseActor ? GetTokenListReq()).mapTo[GetTokenListRes]
      complete(f.map(_.list))
    }
  }

}
