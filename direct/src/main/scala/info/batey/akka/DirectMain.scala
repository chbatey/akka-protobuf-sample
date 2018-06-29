package info.batey.akka

import akka.actor.ActorSystem
import akka.serialization.{SerializationExtension, SerializerWithStringManifest}

object DirectMain extends App {

  val as = ActorSystem()

  val serialization = SerializationExtension(as)

  val post = Post("post1", "The best post", "Cats are great")

  val serializer = serialization.findSerializerFor(post).asInstanceOf[SerializerWithStringManifest]
  assert(serializer.isInstanceOf[PostSerializer])
  assert(serializer.fromBinary(serializer.toBinary(post), serializer.manifest(post)) == post)

  as.terminate()
}
