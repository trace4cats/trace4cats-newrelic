import sbt._

object Dependencies {
  object Versions {
    val scala212 = "2.12.16"
    val scala213 = "2.13.8"
    val scala3 = "3.1.3"

    val trace4cats = "0.13.1+114-c5a4b269"
    
    val trace4catsExporterHttp = "0.13.1+26-3c5516ed"

    val circe = "0.14.2"
    val http4s = "0.23.13"

    val kindProjector = "0.13.2"
    val betterMonadicFor = "0.3.1"
  }

  lazy val trace4catsCore = "io.janstenpickle"         %% "trace4cats-core"          % Versions.trace4cats
  lazy val trace4catsExporterHttp = "io.janstenpickle" %% "trace4cats-exporter-http" % Versions.trace4catsExporterHttp

  lazy val circeCore = "io.circe"     %% "circe-core"   % Versions.circe
  lazy val http4sCirce = "org.http4s" %% "http4s-circe" % Versions.http4s

  lazy val kindProjector = ("org.typelevel" % "kind-projector"     % Versions.kindProjector).cross(CrossVersion.full)
  lazy val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
}
