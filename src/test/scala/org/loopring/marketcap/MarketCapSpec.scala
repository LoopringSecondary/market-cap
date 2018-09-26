package org.loopring.marketcap

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.testkit.{ ImplicitSender, TestKit }
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import scala.concurrent.duration._

abstract class MarketCapSpec
  extends TestKit(ActorSystem("MySpec", ConfigFactory.load()))
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  implicit val timeout = Timeout(5 seconds)

  implicit val mat = ActorMaterializer()

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  // sbt "testOnly *ProxySpec -- -z EchoTest"
  //  "EchoTest" must {
  //    "send back messages unchanged" in {
  //      val echo = system.actorOf(TestActors.echoActorProps)
  //      echo ! "hello world"
  //      expectMsg("hello world")
  //      info("hello test")
  //    }
  //  }

}
