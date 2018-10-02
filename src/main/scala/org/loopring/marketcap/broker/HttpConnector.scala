package org.loopring.marketcap.broker

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.settings.ClientConnectionSettings
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.{ ClientTransport, Http }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ Flow, Sink, Source }
import org.loopring.marketcap._

import scala.concurrent.Future
import scala.reflect.ClassTag

trait HttpConnector extends Json4sSupport {

  implicit val system: ActorSystem
  implicit val mat: ActorMaterializer

  implicit val ex = system.dispatcher

  private[broker] val connection: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]]

  private[broker] val dispatcher = (request: HttpRequest) ⇒
    Source.single(request).via(connection).runWith(Sink.head)

  def https(
    host: String,
    port: Int = 443,
    proxy: Option[Boolean] = None)(
    implicit
    system: ActorSystem): Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
    Http().outgoingConnectionHttps(host, port, settings = getSettings(proxy))
  }

  def http(
    host: String,
    port: Int = 443,
    proxy: Option[Boolean] = None)(
    implicit
    system: ActorSystem): Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
    Http().outgoingConnection(host, port, settings = getSettings(proxy))
  }

  private[this] def getSettings(proxy: Option[Boolean] = None)(implicit system: ActorSystem) = {
    proxy match {
      case Some(true) ⇒
        val httpsProxy = ClientTransport.httpsProxy(
          InetSocketAddress.createUnresolved("127.0.0.1", 1087))
        ClientConnectionSettings(system).withTransport(httpsProxy)
      case _ ⇒ ClientConnectionSettings(system)
    }
  }

  //  implicit def mapToString(response: HttpResponse): Future[String] = {
  //
  //  }

  def get[T](uri: String)(implicit fallback: HttpResponse ⇒ Future[T]): Future[T] = {
    val fallbackFuture = (r: Future[HttpResponse]) ⇒ r.flatMap(fallback)
    (dispatcher andThen fallbackFuture) (Get(uri))
  }

  implicit class ResponseTo(response: HttpResponse) extends Unmarshal[HttpResponse](response) {}

}

object Get {
  def apply(uri: String): HttpRequest =
    HttpRequest(method = HttpMethods.GET, uri = Uri(uri))

  def apply(uri: Uri): HttpRequest =
    HttpRequest(method = HttpMethods.GET, uri = uri)
}

object Post {

  def apply(uri: String): HttpRequest =
    HttpRequest(method = HttpMethods.POST, uri = Uri(uri))

  def apply(uri: Uri): HttpRequest =
    HttpRequest(method = HttpMethods.POST, uri = uri)

}

