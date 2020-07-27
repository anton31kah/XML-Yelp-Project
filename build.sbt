name := "XMLYelpProject"

version := "0.1"

scalaVersion := "2.12.8"

val sparkVersion = "3.0.0"
val sparkAwsVersion = "3.0.0"

val hadoopVersion = "3.2.0"

val redisClientVersion = "3.30"
val memcachedClientVersion = "1.10.0"
val riakClientVersion = "2.1.1"
val dynamoDbClientVersion = "1.0.0-M12-1"

val mode = "local"

// // // //
// Spark //
// // // //
mode match {
  case "local" => libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion
  )
  case "aws" => libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkAwsVersion % "provided",
    "org.apache.spark" %% "spark-sql" % sparkAwsVersion % "provided",
  )
}

// // //  //
// Hadoop //
// // //  //
libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-client" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-aws" % hadoopVersion
)

// // // // // //
// DB  Clients //
// // // // // //
libraryDependencies ++= Seq(
  "net.debasishg" %% "redisclient" % redisClientVersion,
  "io.monix" %% "shade" % memcachedClientVersion,
  "com.basho.riak" % "riak-client" % riakClientVersion
  //  "org.scanamo" %% "scanamo" % dynamoDbClientVersion,
  //  "org.scanamo" %% "scanamo-testkit" % dynamoDbClientVersion,
)
