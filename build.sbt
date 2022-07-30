lazy val commonSettings = Seq(
  Compile / compile / javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _)) =>
        Seq(compilerPlugin(Dependencies.kindProjector), compilerPlugin(Dependencies.betterMonadicFor))
      case _ => Seq.empty
    }
  },
  scalacOptions += {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _)) => "-Wconf:any:wv"
      case _ => "-Wconf:any:v"
    }
  },
  Test / fork := true,
  resolvers ++= Resolver.sonatypeOssRepos("releases"),
)

lazy val noPublishSettings =
  commonSettings ++ Seq(publish := {}, publishArtifact := false, publishTo := None, publish / skip := true)

lazy val publishSettings = commonSettings ++ Seq(
  publishMavenStyle := true,
  pomIncludeRepository := { _ =>
    false
  },
  Test / publishArtifact := false
)

lazy val root = (project in file("."))
  .settings(noPublishSettings)
  .settings(name := "Trace4Cats New Relic")
  .aggregate(`newrelic-http-exporter`)

lazy val `newrelic-http-exporter` =
  (project in file("modules/newrelic-http-exporter"))
    .settings(publishSettings)
    .settings(
      name := "trace4cats-newrelic-http-exporter",
      libraryDependencies ++= Seq(
        Dependencies.circeCore,
        Dependencies.http4sCirce,
        Dependencies.trace4catsCore,
        Dependencies.trace4catsExporterHttp
      )
    )
