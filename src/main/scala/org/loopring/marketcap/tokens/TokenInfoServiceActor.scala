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

package org.loopring.marketcap.tokens

import akka.actor.{ Actor, ActorSystem }
import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.SlickSession
import akka.pattern.pipe
import com.typesafe.scalalogging.LazyLogging
import org.loopring.marketcap.DatabaseAccesser
import org.loopring.marketcap.proto.data._

class TokenInfoServiceActor(
  implicit
  system: ActorSystem,
  mat: ActorMaterializer,
  session: SlickSession)
  extends DatabaseAccesser with Actor with LazyLogging {

  import system.dispatcher
  import session.profile.api._

  override def receive: Receive = {
    case req: GetTokenListReq ⇒

      // TODO(Toan) 这里可能来自数据库或者cache
      implicit val toTokenInfo = (r: ResultRow) ⇒
        TokenInfo(address = r <<, name = r <<, symbol = r <<, website = r <<, decimals = r <<)

      val res =
        sql"""select address, name, symbol, website, decimals
             from t_token_info
             where symbol like
              concat('%', ${req.symbol.getOrElse("")}, '%')
          """.list[TokenInfo] //.map(GetTokenListRes(_))

      res pipeTo sender
  }

}
