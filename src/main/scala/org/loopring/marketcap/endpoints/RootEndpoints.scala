package org.loopring.marketcap.endpoints

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class RootEndpoints {

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
  }

}
