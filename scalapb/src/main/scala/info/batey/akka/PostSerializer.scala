package info.batey.akka

import java.io.NotSerializableException

import akka.serialization.SerializerWithStringManifest

class PostSerializer(val system: akka.actor.ExtendedActorSystem) extends SerializerWithStringManifest {

  private val PostManifest = "P"
  private val MessageManifest = "M"

  override def manifest(o: AnyRef): String = o match {
    case _: MyPost ⇒ PostManifest
    case _: MyMessage ⇒ MessageManifest
    case _ ⇒
      throw new IllegalArgumentException(s"Can't serialize object of type ${o.getClass} in [${getClass.getName}]")
  }

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case a: MyPost ⇒ postToBinary(a)
    case a: MyMessage ⇒ messageToBinary(a)
    case _ ⇒
      throw new IllegalArgumentException(s"Cannot serialize object of type [${o.getClass.getName}]")
  }

  private def postToBinary(a: MyPost): Array[Byte] = {
    BlogPostMessages.Post(a.postId, Some(a.header), Some(a.content)).toByteArray
  }

  private def messageToBinary(a: MyMessage): Array[Byte] = {
    BlogPostMessages.Message(a.messageId, Some(a.content)).toByteArray
  }

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case PostManifest ⇒ postFromBinary(bytes)
    case MessageManifest ⇒ messageFromBinary(bytes)

    case _ ⇒
      throw new NotSerializableException(
        s"Unimplemented deserialization of message with manifest [$manifest] in [${getClass.getName}]")
  }

  private def postFromBinary(bytes: Array[Byte]): MyPost = {
    val a = BlogPostMessages.Post.parseFrom(bytes)
    MyPost(a.postId, a.getTitle, a.getBody)
  }

  private def messageFromBinary(bytes: Array[Byte]): MyMessage = {
    val a = BlogPostMessages.Message.parseFrom(bytes)
    MyMessage(a.messageId, a.getBody)
  }

  override def identifier: Int = 898979
}
