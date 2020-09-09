package preprocessing.jobs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{to_date, year}
import preprocessing.models.json.{Business, Review, User}
import preprocessing.util.JedisHelper._
import preprocessing.util.Paths

import scala.collection.JavaConverters._

object InsertingModel1Redis {
  def main(args: Array[String]): Unit = {
    log("before spark session create")

    implicit val properties: Map[String, String] = readProperties

    val sparkSession = SparkSession.builder
      .appName("Inserting Model1 Redis App")
      .master(properties("spark.url"))
      .getOrCreate()

    import sparkSession.implicits._

    log("spark session created")

    val minYear = 2019

    val businessDf = sparkSession.read
      .json(Paths.SampleData.business)
      .drop("attributes", "postal_code")
      .as[Business]
      .filter(_.business_id != null)

    val reviewDf = sparkSession.read
      .json(Paths.SampleData.review)
      .filter(year(to_date($"date", "yyyy-MM-dd HH:mm:ss")) >= minYear)
      .drop("funny", "cool")
      .as[Review]
      .filter(_.review_id != null)

    val userDf = sparkSession.read
      .json(Paths.SampleData.user)
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

        cluster.hmset(s"business:${business.business_id}", business.toMap.asJava)

        if (business.categories.isDefined) {
          cluster.lpush(s"business:${business.business_id}:categories", business.categories.get.split(", "): _*)
        }

        if (business.hours.isDefined) {
          cluster.hmset(s"business:${business.business_id}:hours", business.hours.get.toMap.asJava)
        }
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

        cluster.hmset(s"user:${user.user_id}", user.toMap.asJava)

        if (user.friends.isDefined) {
          cluster.lpush(s"user:${user.user_id}:friends", user.friends.get.split(", "): _*)
        }
      })

      log(s"after user foreach (count = $count)")
    })

    log("after user foreachPartition")

    log("before review foreachPartition")

    reviewDf.foreachPartition((rows: Iterator[Review]) => {
      log("before review cluster")

      val cluster = createCluster

      log("before review foreach")

      var count = 0

      rows.foreach(review => {
        count += 1

        cluster.hmset(s"review:${review.review_id}", review.toMap.asJava)

        cluster.lpush(s"business:${review.business_id}:reviews", review.review_id)
        cluster.lpush(s"user:${review.user_id}:reviews", review.review_id)
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
