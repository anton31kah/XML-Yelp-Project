package preprocessing.jobs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{to_date, year}
import preprocessing.models.json.{Business, Review, User}
import preprocessing.util.MemcachedHelper._
import preprocessing.util.Paths
import shade.memcached.MemcachedCodecs.AnyRefBinaryCodec

import scala.concurrent.duration.Duration

object InsertingModel1Memcached {
  def main(args: Array[String]): Unit = {
    log("before spark session create")

    implicit val properties: Map[String, String] = readProperties

    val sparkSession = SparkSession.builder
      .appName("Inserting Model1 Memcached App")
      .master(properties("spark.url"))
      .getOrCreate()

    import sparkSession.implicits._

    log("spark session created")

    val minYear = 2019

    val businessDf = sparkSession.read
      .json(Paths.RealData.business)
      .drop("attributes", "postal_code")
      .as[Business]
      .filter(_.business_id != null)

    val reviewDf = sparkSession.read
      .json(Paths.RealData.review)
      .filter(year(to_date($"date", "yyyy-MM-dd HH:mm:ss")) >= minYear)
      .drop("funny", "cool")
      .as[Review]
      .filter(_.review_id != null)

    val userDf = sparkSession.read
      .json(Paths.RealData.user)
      .filter(year(to_date($"yelping_since", "yyyy-MM-dd HH:mm:ss")) >= minYear)
      .drop("useful", "funny", "cool", "elite", "fans")
      .as[User]
      .filter(_.user_id != null)

    log("dataframe created")

    log("before business foreachPartition")

    businessDf.foreachPartition((rows: Iterator[Business]) => {
      log("before business cluster")

      val cluster = createCluster

      log("before business foreach")

      var count = 0

      rows.foreach(business => {
        count += 1

        cluster.set(s"business:${business.business_id}", serialize(business.allToMap), Duration.Inf)
        cluster.add(s"business:${business.business_id}:reviews", "", Duration.Inf)
      })

      log(s"after business foreach (count = $count)")
    })

    log("after business foreachPartition")

    log("before user foreachPartition")

    userDf.foreachPartition((rows: Iterator[User]) => {
      log("before user cluster")

      val cluster = createCluster

      log("before user foreach")

      var count = 0

      rows.foreach(user => {
        count += 1

        cluster.set(s"user:${user.user_id}", serialize(user.allToMap), Duration.Inf)
        cluster.add(s"user:${user.user_id}:reviews", "", Duration.Inf)
      })

      log(s"after user foreach (count = $count)")
    })

    log("after user foreachPartition")

    log("before review foreachPartition")

    reviewDf.foreachPartition((rows: Iterator[Review]) => {
      log("before review cluster")

      val cluster = createCluster
      val client = createClient

      log("before review foreach")

      var count = 0

      rows.foreach(review => {
        count += 1

        cluster.set(s"review:${review.review_id}", serialize(review.allToMap), Duration.Inf)

        client.append(s"business:${review.business_id}:reviews", s"${review.review_id}:")
        client.append(s"user:${review.user_id}:reviews", s"${review.review_id}:")
      })

      log(s"after review foreach (count = $count)")
    })

    log("after review foreachPartition")

    log("done with inserting dataframes")

    log("will stop spark session")

    sparkSession.stop()
    log("stopped spark session")
  }
}
