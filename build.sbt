name := "p2p-crdt"

organization := "edu.kth"

version := "1.0"

scalaVersion := "2.11.11"

val kompicsVersion = "0.9.2-SNAPSHOT"
val kompicsNewVersion = "0.9.4-SNAPSHOT"
val ktoolboxVersion = "2.0.2-SNAPSHOT"

resolvers ++= Seq(
  Resolver.mavenLocal,
  "Kompics Releases" at "http://kompics.sics.se/maven/repository/",
  "Kompics Snapshots" at "http://kompics.sics.se/maven/snapshotrepository/"

)
libraryDependencies ++= Seq(
  // Kompics
  "se.sics.kompics" %% "kompics-scala" % kompicsVersion,
  "se.sics.kompics.basic" % "kompics-component-netty-network" % kompicsVersion,
  "se.sics.kompics.basic" % "kompics-component-java-timer" % kompicsVersion,
  "se.sics.kompics.simulator" % "core" % kompicsNewVersion,

  // K-Toolbox
  "se.sics.ktoolbox.overlaymngr" % "core" % ktoolboxVersion,
  "se.sics.ktoolbox.overlaymngr.bootstrap" % "client" % ktoolboxVersion,
  "se.sics.ktoolbox.overlaymngr.bootstrap" % "server" % ktoolboxVersion,

  // Other
  "ch.qos.logback" % "logback-classic" % "0.9.28",
  "org.scala-lang.modules" %% "scala-pickling" % "0.10.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
)