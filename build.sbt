name := """test"""
organization := "com.test"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"

// database support
//libraryDependencies += jdbc
//libraryDependencies += evolutions
libraryDependencies += "com.zaxxer" % "HikariCP" % "2.5.1"
// database driver
//libraryDependencies += "com.h2database" % "h2" % "1.4.193"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1212"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.test.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.test.binders._"
