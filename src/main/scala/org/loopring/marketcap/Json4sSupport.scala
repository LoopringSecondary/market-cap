package org.loopring.marketcap

import org.json4s.JsonAST.{ JNothing, JNull }
import org.json4s.{ CustomSerializer, DefaultFormats }

trait Json4sSupport extends de.heikoseeberger.akkahttpjson4s.Json4sSupport {

  implicit val serialization = org.json4s.native.Serialization
  implicit val formats = DefaultFormats + EmptyValueSerializer

}

private class EmptyValueSerializer
  extends CustomSerializer[String](
    _ ⇒
      ({
        case JNull ⇒ ""
      }, {
        case "" ⇒ JNothing
      }))

private object EmptyValueSerializer extends EmptyValueSerializer
