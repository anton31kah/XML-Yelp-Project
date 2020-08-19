package preprocessing.jobs

import org.apache.spark.sql.{Row, SparkSession}
import preprocessing.models.json.Business
import preprocessing.util.Paths
import redis.clients.jedis.{HostAndPort, JedisCluster}

import scala.collection.JavaConverters._

object TestAws {
  def main(args: Array[String]): Unit = {
    println("$$$$$ before spark session create")

    val sparkSession = SparkSession.builder
      .appName("Simple Application")
      .master("spark://spark-master:7077")
//       .master("local[*]")
      .getOrCreate()

    import sparkSession.implicits._

    println("$$$$$ spark session created")

    val df = sparkSession.read.json(Paths.SampleData.business)
      .drop("attributes")
      .as[Business]
      .filter(_.business_id != null)

    println("$$$$$ dataframe created")

    df.show(numRows = 15, truncate = false)

    println("$$$$$ show to output")

//    val cluster = new JedisCluster(
//      Set(
//        new HostAndPort("15.161.125.18", 6379),
//        new HostAndPort("15.161.125.18", 6479),
//        new HostAndPort("15.161.90.71", 6379),
//        new HostAndPort("15.161.90.71", 6479),
//        new HostAndPort("15.161.95.163", 6379),
//        new HostAndPort("15.161.95.163", 6479)
//      ).asJava
//    )

//    df.foreach(row => {
//      cluster.hmset(s"business:${row.getAs[String]("business_id")}", Map(
//        "name" -> row.getAs[String]("name"),
//        "address" -> row.getAs[String]("address"),
//        "city" -> row.getAs[String]("city"),
//        "state" -> row.getAs[String]("state"),
//        "latitude" -> row.getAs[String]("latitude"),
//        "longitude" -> row.getAs[String]("longitude"),
//        "stars" -> row.getAs[String]("stars"),
//        "review_count" -> row.getAs[String]("review_count"),
//        "is_open" -> row.getAs[String]("is_open")
//      ).asJava)
//
//      cluster.lpush(
//        s"business:${row.getAs[String]("business_id")}:categories",
//        row.getAs[Seq[String]]("categories"): _*
//      )
//
//      cluster.hmset(
//        s"business:${row.getAs[String]("business_id")}:hours",
//        row.getAs[Map[String, String]]("hours").asJava
//      )
//
//      println("")
//    })

    df.foreachPartition((rows: Iterator[Business]) => {
      println("before cluster")

      val cluster = new JedisCluster(
        Set(
          new HostAndPort("15.161.125.18", 6379),
          new HostAndPort("15.161.125.18", 6479),
          new HostAndPort("15.161.90.71", 6379),
          new HostAndPort("15.161.90.71", 6479),
          new HostAndPort("15.161.95.163", 6379),
          new HostAndPort("15.161.95.163", 6479)
        ).asJava
      )

      println(s"rows = $rows, of type = ${rows.getClass.getName}")

      println(s"cluster ${cluster.getClusterNodes.size()}")

//      rows.foreach(row => {
//        cluster.hmset(s"business:${row.getAs[String]("business_id")}", Map(
//          "name" -> row.getAs[String]("name"),
//          "address" -> row.getAs[String]("address"),
//          "city" -> row.getAs[String]("city"),
//          "state" -> row.getAs[String]("state"),
//          "latitude" -> row.getAs[Double]("latitude").toString,
//          "longitude" -> row.getAs[Double]("longitude").toString,
//          "stars" -> row.getAs[Double]("stars").toString,
//          "review_count" -> row.getAs[Int]("review_count").toString,
//          "is_open" -> row.getAs[Boolean]("is_open").toString
//        ).asJava)
//
//        cluster.lpush(
//          s"business:${row.getAs[String]("business_id")}:categories",
//          row.getAs[String]("categories").split(", "): _*
//        )
//
//        cluster.hmset(
//          s"business:${row.getAs[String]("business_id")}:hours",
//          row.getAs[Hours]("hours").toMap.asJava
//        )
//      })
      rows.foreach(business => {
        cluster.hmset(s"business:${business.business_id}", Map(
          "name" -> business.name,
          "address" -> business.address,
          "city" -> business.city,
          "state" -> business.state,
          "latitude" -> business.latitude,
          "longitude" -> business.longitude,
          "stars" -> business.stars,
          "review_count" -> business.review_count,
          "is_open" -> business.is_open
        ).filter(_._2.isDefined).mapValues(_.get.toString).asJava)

        if (business.categories.isDefined) {
          cluster.lpush(
            s"business:${business.business_id}:categories",
            business.categories.get.split(", "): _*
          )
        }

        if (business.hours.isDefined) {
          cluster.hmset(
            s"business:${business.business_id}:hours",
            business.hours.get.toMap.asJava
          )
        }
      })

    })

//    println("$$$$$ created result")
//
//    result.show(numRows = 15, truncate = false)

//    println("$$$$$ printed result, will write to file")
//
//    result.write
//        .json(Paths.SampleData.tipOutputWriteJson)

//    println("$$$$$ written to json file")
//
//    result.toJSON.write
//      .text(Paths.SampleData.tipOutputToJsonWriteText)
//
//    println("$$$$$ written to text file using toJSON")
//
//    result.saveAsSingleJsonFile(Paths.SampleData.tipOutputToJsonWriteText)
//
//    println("$$$$$ written to a single text file, will stop spark session")
    println("will stop spark session")

    sparkSession.stop()

    println("$$$$$ stopped spark session")
  }
}
