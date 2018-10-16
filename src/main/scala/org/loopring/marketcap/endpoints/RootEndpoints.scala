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
      val f = (tokenInfoDatabaseActor ? GetTokenListReq()).mapTo[Seq[TokenInfo]]
      complete(f)
    }
  }

}
