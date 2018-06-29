package info.batey.akka

import java.io.NotSerializableException

import akka.serialization.{BaseSerializer, SerializerWithStringManifest}

class PostSerializer(val system: akka.actor.ExtendedActorSystem) extends SerializerWithStringManifest {

  private val PostManifest = "P"
  private val MessageManifest = "M"

  override def manifest(o: AnyRef): String = o match {
    case _: Post ⇒ PostManifest
    case _: Message ⇒ MessageManifest
    case _ ⇒
      throw new IllegalArgumentException(s"Can't serialize object of type ${o.getClass} in [${getClass.getName}]")
  }

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case a: Post ⇒ postToBinary(a)
    case a: Message ⇒ messageToBinary(a)
    case _ ⇒
      throw new IllegalArgumentException(s"Cannot serialize object of type [${o.getClass.getName}]")
  }

  private def postToBinary(a: Post): Array[Byte] = {
    val builder = BlogPostMessages.Post.newBuilder()
    builder.setBody(a.body)
    builder.setTitle(a.title)
    builder.setPostId(a.postId)
    builder.build().toByteArray()
  }

  private def messageToBinary(a: Message): Array[Byte] = {
    val builder = BlogPostMessages.Post.newBuilder()
    builder.setBody(a.body)
    builder.setPostId(a.messageId)
    builder.build().toByteArray()
  }

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case PostManifest ⇒ postFromBinary(bytes)
    case MessageManifest ⇒ messageFromBinary(bytes)

    case _ ⇒
      throw new NotSerializableException(
        s"Unimplemented deserialization of message with manifest [$manifest] in [${getClass.getName}]")
  }

  private def postFromBinary(bytes: Array[Byte]): Post = {
    val a = BlogPostMessages.Post.parseFrom(bytes)
    Post(a.getPostId, a.getTitle, a.getBody())
  }

  private def messageFromBinary(bytes: Array[Byte]): Message = {
    val a = BlogPostMessages.Message.parseFrom(bytes)
    Message(a.getMessageId, a.getBody())
  }
  override def identifier: Int = 898989
}
