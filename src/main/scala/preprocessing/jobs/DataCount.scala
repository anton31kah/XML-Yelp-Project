package preprocessing.jobs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{to_date, year}
import preprocessing.util.Paths

object DataCount {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder
      .appName("Simple Application")
      // .master("spark://localhost:7077")
      .master("local[*]")
      .getOrCreate()

    import sparkSession.implicits._

    val businessDf = sparkSession.read.json(Paths.RealData.business)
    val checkinDf = sparkSession.read.json(Paths.RealData.checkin)
    val reviewDf = sparkSession.read.json(Paths.RealData.review)
    val tipDf = sparkSession.read.json(Paths.RealData.tip)
    val userDf = sparkSession.read.json(Paths.RealData.user)

    println(s"businessDf.count() = ${businessDf.count()}")
    println(s"checkinDf.count() = ${checkinDf.count()}")
    println(s"reviewDf.count() = ${reviewDf.count()}")
    println(s"tipDf.count() = ${tipDf.count()}")
    println(s"userDf.count() = ${userDf.count()}")
    /*
    businessDf.count() = 209,393 -> 145 MB // cannot filter
    checkinDf.count() = 175,187 -> 428 MB // cannot filter
    reviewDf.count() = 8,021,122 -> 5.89 GB // can filter on "date"
    tipDf.count() = 1,320,761 -> 251 MB // can filter on "date"
    userDf.count() = 1,968,703 -> 3.04 GB // can filter on "yelping_since"

    total = 9.73 GB

    suggestion: filter reviews, tips, and users, on same date
     */

    val minYear = 2019

    val reviewsAfterYearCount = reviewDf.filter(year(to_date($"date", "yyyy-MM-dd")) >= minYear).count()
    val tipsAfterYearCount = tipDf.filter(year(to_date($"date", "yyyy-MM-dd")) >= minYear).count()
    val usersAfterYearCount = userDf.filter(year(to_date($"yelping_since", "yyyy-MM-dd")) >= minYear).count()

    println(s"reviewDf >= $minYear.count() = $reviewsAfterYearCount")
    println(s"tipDf >= $minYear.count() = $tipsAfterYearCount")
    println(s"userDf >= $minYear.count() = $usersAfterYearCount")
    /*
    reviewDf >= 2018.count() = 2,533,890 ~> 1.86 GB (reduced 3.16 times)
    tipDf >= 2018.count() = 186,384 ~> 35.5 MB (reduced 7.08 times)
    userDf >= 2018.count() = 198,620 ~> 314 MB (reduced 9.91 times)

    total = 2.76 GB (reduced 3.52 times)

    ====================================================================

    reviewDf >= 2019.count() = 1,215,836 ~> 914.2 MB (reduced 6.6 times)
    tipDf >= 2019.count() = 78,558 ~> 14.93 MB (reduced 16.81 times)
    userDf >= 2019.count() = 75,728 ~> 119.75 MB (reduced 26 times)

    total = 1.58 GB (reduced 6.16 times)
     */

    sparkSession.stop()
  }
}
