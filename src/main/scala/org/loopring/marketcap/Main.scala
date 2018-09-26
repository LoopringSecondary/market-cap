package org.loopring.marketcap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{ BinaryMessage, Message, TextMessage, UpgradeToWebSocket }
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, HttpResponse, Uri }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.google.protobuf.Any
import com.typesafe.config.ConfigFactory
import org.loopring.marketcap.proto.data.{ GetTokenMarketCapRes, TokenVolume }
import scalapb.json4s.JsonFormat

object Main extends App {

  //  val config = ConfigFactory.load()
  //
  //  implicit val system = ActorSystem("market-cap", config)
  //  implicit val materializer = ActorMaterializer()

  val d = GetTokenMarketCapRes().withPriceUsd(Seq(TokenVolume.apply(111, "aa")))

  // val d = GetTokenMarketCapRes().withPriceBtc(Seq(TokenVolume(Seq("aa", "bb"))))

  println(JsonFormat.toJsonString(d))

  //  val greeterWebSocketService =
  //    Flow[Message]
  //      .mapConcat {
  //        case tm: TextMessage =>
  //          TextMessage(Source.single("Hello ") ++ tm.textStream) :: Nil
  //        case bm: BinaryMessage =>
  //          bm.dataStream.runWith(Sink.ignore)
  //          Nil
  //      }
  //
  //  val requestHandler: HttpRequest => HttpResponse = {
  //    case req @ HttpRequest(HttpMethods.GET, Uri.Path("/greeter"), _, _, _) =>
  //      req.header[UpgradeToWebSocket] match {
  //        case Some(upgrade) => upgrade.handleMessages(greeterWebSocketService)
  //        case None =>
  //          HttpResponse(400, entity = "Not a valid websocket request!")
  //      }
  //    case r: HttpRequest =>
  //      r.discardEntityBytes() // important to drain incoming HTTP Entity stream
  //      HttpResponse(404, entity = "Unknown resource!")
  //  }
  //
  //  Http().bindAndHandleSync(requestHandler, interface = "localhost", port = 8080)
  //
  //  println("market-cap start")
}
