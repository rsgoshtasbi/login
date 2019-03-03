name := "login"

version := "0.1"

scalaVersion := "2.12.8"
val akkaVersion = "2.5.19"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7"
)
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.5"
libraryDependencies += "net.liftweb" %% "lift-json" % "3.3.0"
