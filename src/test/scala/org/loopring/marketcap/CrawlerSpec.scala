package org.loopring.marketcap

import java.text.SimpleDateFormat
import java.util.Date

import org.jsoup.Jsoup
import org.jsoup.select.NodeFilter
import org.loopring.marketcap
import org.loopring.marketcap.proto.data.TokenIcoInfo
import org.scalatest.FlatSpec
import scalapb.json4s.JsonFormat

class CrawlerSpec extends FlatSpec {

  type JDoc = org.jsoup.nodes.Document

  "test1" should "get html" in {

    val dd = toICO _ compose get


    val ss = dd("https://etherscan.io/token/0xef68e7c694f40c8202821edf525de3782458639f#tokenInfo")


    println("ss ==>>" + JsonFormat.toJsonString(ss))

    //    val f = new SimpleDateFormat("MMM dd, yyyy")
    //
    //    val s = "Aug 01, 2017"
    //
    //    println(f.parse(s))


    //    val doc = get("https://etherscan.io/token/0xef68e7c694f40c8202821edf525de3782458639f#tokenInfo")
    //
    //    val trs = doc.getElementsByTag("tr").asScala
    //
    //    trs.filter(_.childNodeSize() == 7).map { tr ⇒
    //      println(tr.childNodeSize() + "#" + tr.text())
    //    }

  }


  def toICO(doc: JDoc): TokenIcoInfo = {

    import collection.JavaConverters._

    val trs = doc.getElementsByTag("tr").asScala

    val dd = trs.filter(_.childNodeSize() == 7).map { tr ⇒
      (tr.child(0).text().trim → tr.children().last().text())
    } toMap

    val ee = dd.get("ICO Start Date").map(toXX).getOrElse("")
    val ff = dd.get("ICO End Date").map(toXX).getOrElse("")


    val hardCap = dd.get("Hard Cap").getOrElse("").replaceAll("ETH", "").trim
    val softCap = dd.get("Soft Cap").getOrElse("").replaceAll("ETH", "").trim
    val raised = dd.get("Raised").getOrElse("").replaceAll("ETH", "").trim
    val icoPricie = dd.get("ICO Price").getOrElse("").replaceAll("ETH", "").trim
    val country = dd.get("Country").getOrElse("")

    TokenIcoInfo(tokenAddress = "0xef68e7c694f40c8202821edf525de3782458639f",
      icoStartDate = ee,
      icoEndDate = ff,
      hardCap = hardCap,
      softCap = softCap,
      tokenRaised = raised,
      // icoPricie = icoPricie,
      country = country)

  }

  def toXX(s: String): String = {
    val f = new SimpleDateFormat("MMM dd, yyyy")
    f.parse(s).getTime.toString
  }

  def get(url: String): JDoc = Jsoup.connect(url).get()

}
