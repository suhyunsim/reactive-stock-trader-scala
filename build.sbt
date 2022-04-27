ThisBuild / organization := "com.poogle"
ThisBuild / version := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
ThisBuild / scalaVersion := "2.13.8"

// Workaround for scala-java8-compat issue affecting Lagom dev-mode
// https://github.com/lagom/lagom/issues/3344
ThisBuild / libraryDependencySchemes +=
  "org.scala-lang.modules" %% "scala-java8-compat" % VersionScheme.Always

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test

lazy val `reactive-stock-trader-scala` = (project in file("."))
  .aggregate(`reactive-stock-trader-scala-api`, `reactive-stock-trader-scala-impl`, `reactive-stock-trader-scala-stream-api`, `reactive-stock-trader-scala-stream-impl`)

lazy val `reactive-stock-trader-scala-api` = (project in file("reactive-stock-trader-scala-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `reactive-stock-trader-scala-impl` = (project in file("reactive-stock-trader-scala-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`reactive-stock-trader-scala-api`)

lazy val `reactive-stock-trader-scala-stream-api` = (project in file("reactive-stock-trader-scala-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `reactive-stock-trader-scala-stream-impl` = (project in file("reactive-stock-trader-scala-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`reactive-stock-trader-scala-stream-api`, `reactive-stock-trader-scala-api`)
