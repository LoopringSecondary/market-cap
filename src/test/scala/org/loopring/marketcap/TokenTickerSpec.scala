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

package org.loopring.marketcap

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, HttpResponse, StatusCodes }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ FileIO, Sink, Source }

import scala.concurrent.Future

object TokenTickerSpec extends App {

  implicit val system = ActorSystem("TokenTicker")

  implicit val mat = ActorMaterializer()

  implicit val ex = system.dispatcher

  lazy val connection = Http().outgoingConnectionHttps("pro-api.coinmarketcap.com")

  lazy val rawHeader = RawHeader("X-CMC_PRO_API_KEY", "b2e14d15-a592-49a4-8d0d-18bcba5419e7")

  lazy val get = (request: HttpRequest) ⇒ Source.single(request).via(connection).runWith(Sink.head)

  lazy val size = 5000

  lazy val request = (i: Int, currency: String) ⇒ {
    val uri = s"/v1/cryptocurrency/listings/latest?start=${i * size + 1}&limit=${size}&convert=${currency}"
    HttpRequest(uri = uri, method = HttpMethods.GET).withHeaders(rawHeader)
  }

  val dd: Future[String] = get(request(0, "CNY")).flatMap {
    case HttpResponse(StatusCodes.OK, _, entity, _) =>

      entity.dataBytes.runWith(FileIO.toPath(Paths.get("token_cny.json")))

      Future.successful("")

    case _ => Future.successful("")
  }

  dd.onComplete {
    case scala.util.Success(value) ⇒ println(value)
    case scala.util.Failure(exception) ⇒ exception.printStackTrace()
  }

}
