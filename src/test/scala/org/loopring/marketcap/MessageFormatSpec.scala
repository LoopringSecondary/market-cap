package org.loopring.marketcap

import org.json4s.JsonAST.{ JNothing, JNull }
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import org.json4s.{ CustomSerializer, NoTypeHints }
import org.loopring.marketcap.proto.data.{ GetMarketCapReq, _ }
import org.scalatest.FlatSpec
import scalapb.json4s.JsonFormat

class MessageFormatSpec extends FlatSpec {

  implicit val formats = Serialization.formats(NoTypeHints) + EmptyValueSerializer

  "test1" should "print message" in {

    //    val req = GetMarketCapReq(
    //      symbol = "aabbcc",
    //      startTime = Some(10),
    //      // endTime = Some(20),
    //      limit = 100,
    //      intervals = Intervals.DAY_1 //
    //      // intervalVal = GetMarketCapReq.IntervalVal.Intervals(Intervals.DAY_1)
    //      //
    //    )
    //
    //    info(JsonFormat.toJsonString(req))

    // info(write(req))

  }

}

object EmptyValueSerializer extends EmptyValueSerializer

class EmptyValueSerializer
  extends CustomSerializer[String](
    _ ⇒
      ( {
        case JNull ⇒ ""
      }, {
        case "" ⇒ JNothing
      })
  )
