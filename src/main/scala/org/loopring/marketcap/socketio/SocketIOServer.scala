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

package org.loopring.marketcap.socketio

import akka.actor.ActorRef
import com.corundumstudio.socketio.Configuration

class SocketIOServer(port: Int = 9092) {

  private lazy val config = {
    val cfg = new Configuration
    cfg.setHostname("localhost")
    cfg.setPort(port)
    cfg.setMaxFramePayloadLength(1024 * 1024)
    cfg.setMaxHttpContentLength(1024 * 1024)
    cfg.getSocketConfig.setReuseAddress(true)
    cfg
  }

  private lazy val server = new com.corundumstudio.socketio.SocketIOServer(config)

  def registerMessage[I <: ProtoBuf[I], O <: ProtoBuf[O]](event: String, actorRef: ActorRef)(
    implicit
    c: scalapb.GeneratedMessageCompanion[I]): Unit = {
    implicit def _string2ProtoBuf = string2ProtoBuf[I]

    implicit def _protoBuf2JavaMap = protoBuf2JavaMap[O]

    server.addEventListener(event, classOf[Any], new DataListener[I, O](event, actorRef))
  }

  def registerEvent[I, O](event: String, actorRef: ActorRef)(
    implicit
    serializr: String ⇒ I,
    deserializr: O ⇒ java.util.Map[String, Any]): Unit = {
    server.addEventListener(event, classOf[Any], new DataListener[I, O](event, actorRef))
  }

  def broadcastMessage[T](msg: BroadcastMessage[T])(
    implicit
    deserializr: T ⇒ java.util.Map[String, Any]): Unit = {
    val map = deserializr(msg.t)
    server.getBroadcastOperations.sendEvent(msg.event, map)
  }

  def start: Unit = server.start()

  def stop: Unit = server.stop()

}
