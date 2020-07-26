name := "XMLYelpProject"

version := "0.1"

scalaVersion := "2.12.8"

val sparkVersion = "3.0.0"
val sparkAwsVersion = "3.0.0"

val mode = "aws"

mode match {
  case "local" => libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion
  )
  case "aws" => libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkAwsVersion % "provided",
    "org.apache.spark" %% "spark-sql" % sparkAwsVersion % "provided",
//    "org.apache.hadoop" % "hadoop-common" % "2.7.0",
//    "org.apache.hadoop" % "hadoop-client" % "2.7.0",
//    "org.apache.hadoop" % "hadoop-aws" % "2.7.0",
  )
}
