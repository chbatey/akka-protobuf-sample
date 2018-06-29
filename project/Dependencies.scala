import sbt._

object Dependencies {

  val akkaVersion = "2.5.13"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val protobufRuntime = "com.google.protobuf" % "protobuf-java" % "3.5.1"
}
