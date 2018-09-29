package org.loopring.marketcap.crawler

import akka.actor.{ Actor, ActorSystem }
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.SlickSession
import org.loopring.marketcap.DatabaseAccesser
import org.loopring.marketcap.proto.data._

class TokenIcoServiceActor(
  implicit
  system: ActorSystem,
  mat: ActorMaterializer,
  session: SlickSession) extends DatabaseAccesser with Actor {

  import session.profile.api._
  import system.dispatcher

  override def receive: Receive = {
    case info: TokenIcoInfo ⇒

      implicit val saveTokenIcoInfo = (info: TokenIcoInfo) ⇒
        sqlu"""INSERT INTO t_token_ico_onfo(token_address, ico_start_date,
          ico_end_date, hard_cap, soft_cap, token_raised, ico_price, from_country) VALUES(
          ${info.tokenAddress}, ${info.icoStartDate}, ${info.icoEndDate}, ${info.hardCap},
          ${info.softCap}, ${info.raised}, ${info.icoPrice}, ${info.country})"""

      saveOrUpdate(info)

    case req: GetTokenIcoInfoReq ⇒

      implicit val toGetTokenIcoInfo = (r: ResultSet) ⇒
        TokenIcoInfo(tokenAddress = r <<, icoStartDate = r <<, icoEndDate = r <<,
          hardCap = r <<, softCap = r <<, raised = r <<, icoPrice = r <<, country = r <<)

      // TODO(Toan) 这里缺少where条件
      val res =
        sql"""SELECT token_address, ico_start_date, ico_end_date, hard_cap, soft_cap, token_raised,
             ico_price, from_country from t_token_ico_info"""
          .list[TokenIcoInfo].map(GetTokenIcoInfoRes(_))

      res pipeTo sender

  }
}
