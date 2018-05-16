name := "reactive.tcp.client"
organization := "snapptrip"
version := "1.0"
scalaVersion := "2.12.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion = "2.5.12"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion
  )
}

enablePlugins(JavaAppPackaging)

Revolver.settings

