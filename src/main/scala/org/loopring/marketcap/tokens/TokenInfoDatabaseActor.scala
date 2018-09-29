package org.loopring.marketcap.tokens

import akka.actor.Actor
import akka.stream.ActorMaterializer
import akka.stream.alpakka.slick.scaladsl.SlickSession
import org.loopring.marketcap.DatabaseAccesser
import org.loopring.marketcap.proto.data.GetTokenListReq

import scala.concurrent.ExecutionContextExecutor

class TokenInfoActor(
  implicit
  mat: ActorMaterializer,
  ec: ExecutionContextExecutor,
  session: SlickSession
) extends DatabaseAccesser with Actor {

  override def receive: Receive = {
    case req: GetTokenListReq ⇒
  }

}
