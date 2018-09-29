package org.loopring.marketcap

import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{ Flow, Source }

class WebSocketEndpoints {

  def apply(): Route = {
    path("ws") {
      handleWebSocketMessages(flow)
    }
  }

  private def flow: Flow[Message, Message, Any] =
    Flow[Message].mapConcat {
      case tm: TextMessage =>
        TextMessage(Source.single("Hello ") ++ tm.textStream ++ Source.single("!")) :: Nil
      case _ ⇒ Nil
    }

}
