name := "word-stream-BE"

enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies ++= Seq(
  jdbc, // Required for database access (even if not used)
  ehcache, // Caching library (optional)
  ws, // Play Web Services (WSClient)
  specs2 % Test, // Testing framework
  guice //  Dependency injection (required for Play)
)
