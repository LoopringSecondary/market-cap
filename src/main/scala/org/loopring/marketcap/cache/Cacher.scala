//package org.loopring.marketcap.cache
//
//import akka.util.ByteString
//import org.loopring.marketcap._
//import redis.{ ByteStringDeserializer, ByteStringSerializer, RedisClient, RedisCluster }
//
//import scala.concurrent.{ ExecutionContextExecutor, Future }
//
//trait Cacher[T] {
//
//  def get(k: String): Future[Option[T]]
//
//  def getOrElse(k: String)(fallback: ⇒ Future[Option[T]]): Future[Option[T]]
//
//  def put(k: String, v: T): Future[Boolean]
//
//  def put(k: String, v: T, ttl: Long): Future[Boolean]
//
//}
//
//class ProtoBufMessageCacher[T <: ProtoBuf[T]](
//  implicit
//  redis: RedisCluster,
//  ec: ExecutionContextExecutor,
//  c: scalapb.GeneratedMessageCompanion[T]
//) extends Cacher[T] {
//
//  private[this] implicit val deserializer = new ProtoBufByteStringDeserializer[T]
//
//  private[this] implicit val serializer = new ProtoBufByteStringSerializer[T]
//
//  def get(k: String): Future[Option[T]] = redis.get(k)
//
//  def getOrElse(k: String)(fallback: ⇒ Future[Option[T]]): Future[Option[T]] = {
//
//    for {
//      tOption ← redis.get(k)
//      fallbackOption ← if (tOption.isEmpty) fallback else Future.successful(tOption)
//      _ ← if (fallbackOption.isDefined) redis.set(k, fallbackOption.get) else Future.unit
//    } yield fallbackOption
//
//  }
//
//  override def put(k: String, v: T): Future[Boolean] = redis.set(k, v)
//
//  override def put(k: String, v: T, ttl: Long): Future[Boolean] = redis.set(k, v, Some(ttl))
//}
//
//final class ProtoBufByteStringDeserializer[T <: ProtoBuf[T]](
//  implicit c: scalapb.GeneratedMessageCompanion[T]
//)
//  extends ByteStringDeserializer[T] {
//
//  override def deserialize(bs: ByteString): T = c.parseFrom(bs.toArray)
//}
//
//final class ProtoBufByteStringSerializer[T <: ProtoBuf[T]] extends ByteStringSerializer[T] {
//
//  override def serialize(data: T): ByteString = ByteString.fromArray(data.toByteArray)
//
//}
//
//
//
