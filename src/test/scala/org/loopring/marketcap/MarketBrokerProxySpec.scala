package org.loopring.marketcap

import akka.actor.Props
import akka.pattern.ask
import org.loopring.marketcap.broker.MarketBrokerProxy

import scala.concurrent.Await

class MarketBrokerProxySpec extends MarketCapSpec {

  val broker = system.actorOf(Props(new MarketBrokerProxy()))

  // sbt "testOnly *ProxySpec -- -z EchoTest"
  "EchoTest" must {
    "send back messages unchanged" in {
      val resultFuture = (broker ? "hello world")

      val res = Await.result(resultFuture, timeout.duration)

      info(res.toString)
    }
  }

}
