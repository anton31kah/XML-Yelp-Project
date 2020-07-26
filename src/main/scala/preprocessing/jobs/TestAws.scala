package preprocessing.jobs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{year, to_date}
import preprocessing.util.Paths
import preprocessing.util.SparkHelper._

object TestAws {
  def main(args: Array[String]): Unit = {
    println("$$$$$ before spark session create")

    val sparkSession = SparkSession.builder
      .appName("Simple Application")
      .master("spark://spark-master:7077")
//       .master("local[*]")
      .getOrCreate()

    println("$$$$$ spark session created")

    import sparkSession.implicits._

    val df = sparkSession.read.json(Paths.SampleData.tip)

    println("$$$$$ dataframe created")

    df.show(numRows = 15, truncate = false)

    println("$$$$$ show to output")

    val result = df
      .filter(year(to_date($"date")) >= 2013)
      .select($"text", $"date")

    println("$$$$$ created result")

    result.show(numRows = 15, truncate = false)

    println("$$$$$ printed result, will write to file")

    result.write
        .json(Paths.SampleData.tipOutputWriteJson)

    println("$$$$$ written to json file")

    result.toJSON.write
      .text(Paths.SampleData.tipOutputToJsonWriteText)

    println("$$$$$ written to text file using toJSON")

    result.saveAsSingleJsonFile(Paths.SampleData.tipOutputToJsonWriteText)

    println("$$$$$ written to a single text file, will stop spark session")

    sparkSession.stop()

    println("$$$$$ stopped spark session")
  }
}
