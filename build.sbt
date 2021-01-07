name := "scala-training"
organization := "maalvarez.info"

version := "0.1"
scalaVersion := "2.13.4"

val Versions = new {
  val Circe = "0.12.3"
  val Cats = "2.1.1"
  val ScalaTest = "3.0.8"
}
val dependencies: Seq[ModuleID] = List(
  "io.circe" %% "circe-core" % Versions.Circe,
  "io.circe" %% "circe-parser" % Versions.Circe,
  "io.circe" %% "circe-generic" % Versions.Circe,
  "org.typelevel" %% "cats-core" % Versions.Cats,
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"
)

val testDependencies: Seq[ModuleID] = List(
  "org.scalatest" %% "scalatest" % Versions.ScalaTest
).map(_ % "test")

libraryDependencies ++= dependencies ++ testDependencies