package org.loopring.marketcap.broker

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.FromConfig
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

class MarketBrokerProxy(implicit sys: ActorSystem, mat: ActorMaterializer)
    extends Actor
    with LazyLogging {

  val binance =
    sys.actorOf(
      FromConfig.props(Props(new BinanceMarketBroker())),
      "binance_broker"
    )

  override def receive: Receive = {
    case "binance" ⇒
      binance.forward("binance")
    case "okex" ⇒
      binance.forward("okex")
  }

}
