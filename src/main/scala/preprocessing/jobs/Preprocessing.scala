package preprocessing.jobs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{to_date, year}
import preprocessing.models.json.Business
import preprocessing.util.Paths
import preprocessing.util.SparkHelper._

object Preprocessing {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder
      .appName("Simple Application")
      // .master("spark://localhost:7077")
      .master("local[*]")
      .getOrCreate()

    import sparkSession.implicits._

    // crlf
    val businessDf = sparkSession.read.json(Paths.SampleData.business)
      .drop("attributes")
      .as[Business]
      .filter(_.business_id != null)
    //    val checkinDf = sparkSession.read.json(Paths.SampleData.checkin).as[Checkin]
    //    val reviewDf = sparkSession.read.json(Paths.SampleData.review).as[Review]
    //    val tipDf = sparkSession.read.json(Paths.SampleData.tip).as[Tip]
    //    val tipDf = sparkSession.read.json(Paths.RealData.tip)
    //    val userDf = sparkSession.read.json(Paths.SampleData.user).as[User]

    val minYear = 2019

    businessDf.foreachPartition((businesses: Iterator[Business]) => {
      businesses.foreach(println)
    })

//    tipDf.filter(year(to_date($"date", "yyyy-MM-dd")) >= minYear)
//      .saveAsSingleJsonFile(Paths.RealData.tips2019)
//      .show(numRows = 1000, truncate = false)

    sparkSession.stop()
  }
}
