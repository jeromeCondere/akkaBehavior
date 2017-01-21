name := "akkaBehavior"

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.3" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "com.typesafe.akka" %% "akka-slf4j"   % "2.4.16",
  "com.typesafe.akka" %% "akka-remote"  % "2.4.16",
  "com.typesafe.akka" %% "akka-agent"   % "2.4.16",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.16" % "test"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
