package org.loopring.marketcap.tokens

import akka.actor.{ Actor, ActorSystem }
import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.SlickSession
import akka.pattern.pipe
import org.loopring.marketcap.DatabaseAccesser
import org.loopring.marketcap.proto.data._

class TokenInfoServiceActor(
  implicit
  system: ActorSystem,
  mat: ActorMaterializer,
  session: SlickSession) extends DatabaseAccesser with Actor {

  import system.dispatcher
  import session.profile.api._

  override def receive: Receive = {
    case req: GetTokenListReq ⇒

      implicit val toTokenInfo = (r: ResultSet) ⇒
        TokenInfo(address = r <<, name = r <<, symbol = r <<, website = r <<, decimals = r <<)

      val res =
        sql"""select address, name, symbol, website, decimals
             from t_token_info
             where symbol like
              concat('%', ${req.symbol.getOrElse("")}, '%')
          """.list[TokenInfo].map(GetTokenListRes(_))

      res pipeTo sender
  }

}
