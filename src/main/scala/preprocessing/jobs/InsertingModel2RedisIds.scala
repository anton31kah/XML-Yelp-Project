package preprocessing.jobs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{to_date, year, concat, lit}
import preprocessing.util.JedisHelper._
import preprocessing.util.Paths

object InsertingModel2RedisIds {
  def main(args: Array[String]): Unit = {
    log("before spark session create")

    implicit val properties: Map[String, String] = readProperties

    val sparkSession = SparkSession.builder
      .appName("Inserting Model2 Redis App")
      .master(properties("spark.url"))
      .getOrCreate()

    import sparkSession.implicits._

    log("spark session created")

    val minYear = 2019

    sparkSession.read
      .json(Paths.RealData.business)
      .filter($"business_id".isNotNull)
      .select($"business_id")
      .write.text(Paths.RealData.businessIds)

    // hgetall business:id
    // lrange business:id:categories
    // hgetall business:id:hours
    // lrange business:id:reviews

    sparkSession.read
      .json(Paths.RealData.review)
      .filter($"review_id".isNotNull and year(to_date($"date", "yyyy-MM-dd HH:mm:ss")) >= minYear)
      .select(concat($"review_id", lit(" "), $"business_id"))
      .write.text(Paths.RealData.reviewIds)

    // hgetall review:id

    sparkSession.read
      .json(Paths.RealData.user)
      .filter($"user_id".isNotNull and year(to_date($"yelping_since", "yyyy-MM-dd HH:mm:ss")) >= minYear)
      .select($"user_id")
      .write.text(Paths.RealData.userIds)

    // hgetall user:id
    // lrange user:id:friends
    // lrange user:id:reviews

    log("will stop spark session")

    sparkSession.stop()
    log("stopped spark session")
  }
}
