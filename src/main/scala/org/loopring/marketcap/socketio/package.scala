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

import com.fasterxml.jackson.databind.ObjectMapper
import scalapb.json4s.JsonFormat

package object socketio {

  implicit def string2ProtoBuf[T <: ProtoBuf[T]](
    implicit
    c: scalapb.GeneratedMessageCompanion[T]) = (json: String) ⇒ JsonFormat.fromJsonString[T](json)

  implicit def protoBuf2JavaMap[T <: ProtoBuf[T]] = (t: T) ⇒ {
    val mapper = new ObjectMapper()
    mapper.readValue(JsonFormat.toJsonString(t), classOf[java.util.Map[String, Any]])
  }

}
