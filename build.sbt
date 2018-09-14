import sbt.Keys.name

val projectSettings = Seq(
  organization := "com.myorganization",
  name := "base-playfm",
  scalaVersion := "2.11.8",
  version := "1.0"
)

lazy val root = (project in file("."))
  .settings( projectSettings )
  .enablePlugins(PlayScala)

// Dependencies
libraryDependencies ++= Seq(
  guice,
  ehcache,
  cacheApi,
  play.sbt.PlayImport.cacheApi,
  "com.typesafe.play"       %% "play-slick"               % "3.0.1",
  "com.typesafe.play"       %% "play-slick-evolutions"    % "3.0.1",
  "com.typesafe.play"       %% "play-json"                % "2.6.0",
  "org.typelevel"           %% "cats-core"                % "1.0.1",
  "com.typesafe"            %  "config"                   % "1.3.2",
  "org.scalactic"           %% "scalactic"                % "3.0.5",
  "org.scalatest"           %% "scalatest"                % "3.0.5",
  "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.2" % Test,
  "com.typesafe.slick"      %% "slick-extensions"         % "3.1.0",
  "mysql"                   %  "mysql-connector-java"     % "5.1.34",
  "com.h2database"          %  "h2"                       % "1.4.189",
  "com.github.karelcemus"   %% "play-redis"               % "2.1.1",
  "org.dispatchhttp"        %% "dispatch-core"            % "0.14.0"
)

// Tasks
fork in run := true