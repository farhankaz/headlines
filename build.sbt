val Http4sVersion = "0.20.0-M5"
val CirceVersion = "0.10.0"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "headlines",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-language:higherKinds",
      "-language:postfixOps",
      "-feature",
      "-Ypartial-unification",
    ),
    libraryDependencies ++= Seq(
      "org.http4s"        %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"        %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"        %% "http4s-circe"        % Http4sVersion,
      "org.http4s"        %% "http4s-dsl"          % Http4sVersion,
      "io.circe"          %% "circe-generic"       % CirceVersion,
      "org.specs2"        %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback"    %  "logback-classic"     % LogbackVersion,
      "org.typelevel"     %% "cats-effect" % "1.2.0",
      "io.monix"          %% "monix-eval" % "3.0.0-RC2",
      "io.monix"          %% "monix-catnap" % "3.0.0-RC2",
      "net.ruippeixotog"  %% "scala-scraper"     % "2.1.0"

    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
  )

