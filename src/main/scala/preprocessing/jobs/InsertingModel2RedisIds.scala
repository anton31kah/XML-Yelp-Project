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
      .appName("Inserting Model2 Redis App Getting Ids")
      .master(properties("spark.url"))
      .getOrCreate()

    import sparkSession.implicits._

    log("spark session created")

    val minYear = 2019

    sparkSession.read
      .json(Paths.RealData.review)
      .filter($"review_id".isNotNull and year(to_date($"date", "yyyy-MM-dd HH:mm:ss")) >= minYear)
      .select(concat($"review_id", lit(" "), $"business_id"))
      .write.text(Paths.RealData.reviewIds)

    // hgetall business:id:review:id

    log("will stop spark session")

    sparkSession.stop()
    log("stopped spark session")
  }
}
