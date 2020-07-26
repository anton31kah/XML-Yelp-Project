package preprocessing.jobs

import org.apache.spark.sql.SparkSession
import preprocessing.util.Paths

object BusinessAttributesAnalysis {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder
      .appName("Simple Application")
      // .master("spark://localhost:7077")
      .master("local[*]")
      .getOrCreate()

    import sparkSession.implicits._

    val businessDf = sparkSession.read.json(Paths.RealData.business)

    val columnExplored = $"attributes.Music"

    businessDf
      .select(columnExplored)
      .filter(columnExplored.isNotNull)
      .distinct()
//      .flatMap(b => explode($"attributes"))
//      .printSchema()
      .show(numRows = Int.MaxValue, truncate = false)
//      .rdd
//      .saveAsSingleTextFile(Paths.RealData.businessAttributes)

    sparkSession.stop()
  }
}
