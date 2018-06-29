import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "info.batey",
      scalaVersion := "2.12.6",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "akka-protobuf-sample",
    libraryDependencies += scalaTest % Test
  )

lazy val direct = project
  .settings(
    name := "direct",
    libraryDependencies ++= Seq(
      akka,
      protobufRuntime
    )
  )

lazy val scalapbExample = project
  .in(file("scalapb"))
  .settings(
    name := "scalapb",
    libraryDependencies ++= Seq(
      akka
    ),
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    )
  )

