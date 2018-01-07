enablePlugins(JavaAppPackaging)

name := "tcp.client"
organization := "pintapin"
version := "1.0"
scalaVersion := "2.12.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion = "2.5.8"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion
  )
}

Revolver.settings

