//package org.loopring.marketcap
//import java.net.InetSocketAddress
//
//import akka.actor.ActorSystem
//import akka.http.scaladsl.{ClientTransport, Http}
//import akka.http.scaladsl.model.{HttpHeader, HttpRequest, HttpResponse}
//import akka.http.scaladsl.settings.{
//  ClientConnectionSettings,
//  ConnectionPoolSettings
//}
//
//import scala.concurrent.Future
//import scala.collection._
//
//trait HttpConnector {
//
//  implicit val sys: ActorSystem
//
//  private lazy val proxyHost = "127.0.0.1"
//  private lazy val proxyPort = 1087
//
//  lazy val httpsProxyTransport = ClientTransport.httpsProxy(
//    InetSocketAddress.createUnresolved(proxyHost, proxyPort)
//  )
//
//  def settings(proxy: Boolean = false) =
//    ConnectionPoolSettings(sys)
//      .withConnectionSettings(
//        ClientConnectionSettings(sys)
//          .withTransport(
//            if (proxy) httpsProxyTransport
//            else ClientTransport.TCP
//          )
//      )
//
//  def get(uri: String,
//          headers: immutable.Seq[HttpHeader]): Future[HttpResponse] =
//    get(uri, false, immutable.Seq.empty[HttpHeader])
//
//  def get(uri: String, proxy: Boolean = false): Future[HttpResponse] =
//    get(uri, proxy, immutable.Seq.empty[HttpHeader])
//
//  def get(uri: String,
//          proxy: Boolean = false,
//          headers: immutable.Seq[HttpHeader]): Future[HttpResponse] =
//    Http().singleRequest(
//      HttpRequest(uri = uri, headers = headers),
//      settings = settings(proxy)
//    )
//
//}
