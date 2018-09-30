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

import akka.actor.ActorSystem
import akka.util.ByteString
import redis.{ ByteStringDeserializer, ByteStringSerializer, RedisCluster }

import scala.concurrent.Future

class ProtoBufMessageCacher[T <: ProtoBuf[T]](
  implicit
  redis: RedisCluster,
  system: ActorSystem,
  c: scalapb.GeneratedMessageCompanion[T]) {

  private[this] implicit val deserializer = new ProtoBufByteStringDeserializer[T]

  private[this] implicit val serializer = new ProtoBufByteStringSerializer[T]

  def get(k: String): Future[Option[T]] = redis.get(k)

  def getOrElse(k: String, ttl: Option[Long] = None)(fallback: ⇒ Future[Option[T]]): Future[Option[T]] = {

    for {
      tOption ← redis.get(k)
      fallbackOption ← if (tOption.isEmpty) fallback else Future.successful(tOption)
      _ ← if (fallbackOption.isDefined) redis.set(k, fallbackOption.get, exSeconds = ttl) else Future.unit
    } yield fallbackOption

  }

  def put(k: String, v: T): Future[Boolean] = redis.set(k, v)

  def put(k: String, v: T, ttl: Long): Future[Boolean] = redis.set(k, v, exSeconds = Some(ttl))

  def push(k: String, vs: Seq[T]): Future[Long] =
    redis.lpush(k, vs.map(serializer.serialize): _*)

  def pull(k: String, start: Long = 0, stop: Long = -1): Future[Seq[T]] =
    redis.lrange(k, start, stop)

}

final class ProtoBufByteStringDeserializer[T <: ProtoBuf[T]](
  implicit
  c: scalapb.GeneratedMessageCompanion[T])
  extends ByteStringDeserializer[T] {

  override def deserialize(bs: ByteString): T = c.parseFrom(bs.toArray)
}

final class ProtoBufByteStringSerializer[T <: ProtoBuf[T]] extends ByteStringSerializer[T] {

  override def serialize(data: T): ByteString = ByteString.fromArray(data.toByteArray)

}
