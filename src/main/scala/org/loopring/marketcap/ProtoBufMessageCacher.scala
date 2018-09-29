package org.loopring.marketcap

import akka.util.ByteString
import redis.{ ByteStringDeserializer, ByteStringSerializer, RedisCluster }

import scala.concurrent.{ ExecutionContextExecutor, Future }

class ProtoBufMessageCacher[T <: ProtoBuf[T]](
  implicit
  redis: RedisCluster,
  ec: ExecutionContextExecutor,
  c: scalapb.GeneratedMessageCompanion[T]) {

  private[this] implicit val deserializer = new ProtoBufByteStringDeserializer[T]

  private[this] implicit val serializer = new ProtoBufByteStringSerializer[T]

  def get(k: String): Future[Option[T]] = redis.get(k)

  def getOrElse(k: String)(fallback: ⇒ Future[Option[T]]): Future[Option[T]] = {

    for {
      tOption ← redis.get(k)
      fallbackOption ← if (tOption.isEmpty) fallback else Future.successful(tOption)
      _ ← if (fallbackOption.isDefined) redis.set(k, fallbackOption.get) else Future.unit
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
