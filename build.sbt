name := "XMLYelpProject"

version := "0.1"

scalaVersion := "2.12.8"

val sparkVersion    = "3.0.0"
val sparkAwsVersion = "3.0.0"

val hadoopVersion = "3.2.0"

val redisClientVersion      = "3.30"
val redisJedisClientVersion = "3.3.0"
val memcachedClientVersion  = "1.10.0"
val riakClientVersion       = "2.1.1"
val dynamoDbClientVersion   = "1.0.0-M12-1"

val zioVersion = "1.0.0-RC21-2"

val mode = "local"

// // // //
// Spark //
// // // //
mode match {
  case "local" =>
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql"  % sparkVersion
    )
  case "aws" =>
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkAwsVersion % "provided",
      "org.apache.spark" %% "spark-sql"  % sparkAwsVersion % "provided"
    )
}

// // //  //
// Hadoop //
// // //  //
//libraryDependencies ++= Seq(
//  "org.apache.hadoop" % "hadoop-common" % hadoopVersion,
//  "org.apache.hadoop" % "hadoop-client" % hadoopVersion,
//  "org.apache.hadoop" % "hadoop-aws" % hadoopVersion
//)

// // // // // //
// DB  Clients //
// // // // // //
libraryDependencies ++= Seq(
//  "net.debasishg" %% "redisclient" % redisClientVersion,
  "redis.clients"  % "jedis"       % redisJedisClientVersion,
  "io.monix"      %% "shade"       % memcachedClientVersion,
  "com.basho.riak" % "riak-client" % riakClientVersion
  //  "org.scanamo" %% "scanamo" % dynamoDbClientVersion,
  //  "org.scanamo" %% "scanamo-testkit" % dynamoDbClientVersion,
)

//  // //
// Zio //
//  // //
//libraryDependencies ++= Seq(
//  "dev.zio" %% "zio" % zioVersion
//)

assemblyMergeStrategy in assembly := {
  case PathList("redis", "clients", xs @ _*)            => MergeStrategy.last
  case PathList("io", "monix", xs @ _*)                 => MergeStrategy.last
  case PathList("com", "basho", xs @ _*)                => MergeStrategy.last
  case PathList("org", "apache", xs @ _*)               => MergeStrategy.last
  case PathList("org", "apache", "arrow", xs @ _*)      => MergeStrategy.last
  case PathList("com", "sun", xs @ _*)                  => MergeStrategy.last
  case PathList("jakarta", "activation", xs @ _*)       => MergeStrategy.last
  case PathList("jakarta", "xml", xs @ _*)              => MergeStrategy.last
  case PathList("jakarta", "ws", xs @ _*)               => MergeStrategy.last
  case PathList("org", "aopalliance", xs @ _*)          => MergeStrategy.last
  case PathList("org", "threeten", xs @ _*)             => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*)             => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*)            => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*)         => MergeStrategy.last
  case PathList("javax", "xml", xs @ _*)                => MergeStrategy.last
  case PathList("com", "google", xs @ _*)               => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*)     => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*)             => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*)               => MergeStrategy.last
  case PathList("com", "fasterxml", "jackson", xs @ _*) => MergeStrategy.last
  case PathList("META-INF", xs @ _*)                    => MergeStrategy.discard
//  case PathList("META-INF", xs @ _*) =>
//    xs map {_.toLowerCase} match {
//      case "manifest.mf" :: Nil | "index.list" :: Nil | "dependencies" :: Nil =>
//        MergeStrategy.discard
//      case ps @ x :: xs if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
//        MergeStrategy.discard
//      case "plexus" :: xs =>
//        MergeStrategy.discard
//      case "services" :: xs =>
//        MergeStrategy.filterDistinctLines
//      case "spring.schemas" :: Nil | "spring.handlers" :: Nil =>
//        MergeStrategy.filterDistinctLines
//      case _ => MergeStrategy.first
//    }
  case "application.conf"                               => MergeStrategy.concat
  case "reference.conf"                                 => MergeStrategy.concat
  case _                                                => MergeStrategy.first
//  case x =>
//    val baseStrategy = (assemblyMergeStrategy in assembly).value
//    baseStrategy(x)
}
