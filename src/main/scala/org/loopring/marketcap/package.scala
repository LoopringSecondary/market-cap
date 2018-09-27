package org.loopring

package object marketcap {

  type Bytes = Array[Byte]

  type ProtoBuf[T] = scalapb.GeneratedMessage with scalapb.Message[T]

}
