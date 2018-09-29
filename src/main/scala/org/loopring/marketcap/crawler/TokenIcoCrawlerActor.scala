package org.loopring.marketcap.crawler

import java.text.SimpleDateFormat

import akka.actor.{ Actor, ActorRef, Timers }
import org.jsoup.Jsoup
import org.loopring.marketcap.proto.data.TokenIcoInfo

import scala.concurrent.duration._

class TokenIcoCrawlerActor(
  dbAccess: ActorRef // TokenIcoDatabaseActor
) extends Actor with Timers {

  type JDoc = org.jsoup.nodes.Document

  override def preStart(): Unit = {

    //TODO(Toan) 这里需要添加消息
    timers.startSingleTimer("", "", 5 seconds)
  }

  override def receive: Receive = {
    case _: String ⇒

      import collection.JavaConverters._

      val doc = get("https://etherscan.io/token/0xef68e7c694f40c8202821edf525de3782458639f#tokenInfo")

      val trs = doc.getElementsByTag("tr").asScala

      val tdsMap = trs.filter(_.childNodeSize() == 7).map { tr ⇒
        val childs = tr.children()
        (childs.first().text().trim → childs.last().text().trim)
      } toMap

      val icoStartDate = tdsMap.get("ICO Start Date").map(toUnixtime).getOrElse("")
      val icoEndDate = tdsMap.get("ICO End Date").map(toUnixtime).getOrElse("")

      val hardCap = tdsMap.get("Hard Cap").map(toTrimEth).getOrElse("")
      val softCap = tdsMap.get("Soft Cap").map(toTrimEth).getOrElse("")
      val raised = tdsMap.get("Raised").map(toTrimEth).getOrElse("")
      val icoPrice = tdsMap.get("ICO Price").map(toTrimEth).getOrElse("")
      val country = tdsMap.get("Country").getOrElse("")

      val tokenIcoInfo = TokenIcoInfo(
        tokenAddress = "0xef68e7c694f40c8202821edf525de3782458639f",
        icoStartDate = icoStartDate,
        icoEndDate = icoEndDate,
        hardCap = hardCap,
        softCap = softCap,
        raised = raised,
        icoPrice = icoPrice,
        country = country)

      dbAccess ! tokenIcoInfo

  }

  private def toUnixtime: PartialFunction[String, String] = {
    case str: String ⇒
      val format = new SimpleDateFormat("MMM dd, yyyy")
      format.parse(str).getTime.toString
  }

  private def toTrimEth: PartialFunction[String, String] = {
    case str: String ⇒ str.replaceAll("ETH", "").trim
  }

  private def get(url: String): JDoc = Jsoup.connect(url).get()

}
