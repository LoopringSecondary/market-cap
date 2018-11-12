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

import org.json4s.JsonAST.{ JNothing, JNull }
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import org.json4s.{ CustomSerializer, NoTypeHints }
import org.loopring.marketcap.proto.data._
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
      ({
        case JNull ⇒ ""
      }, {
        case "" ⇒ JNothing
      }))
