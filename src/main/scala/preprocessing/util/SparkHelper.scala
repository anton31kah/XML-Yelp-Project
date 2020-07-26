package preprocessing.util

import org.apache.hadoop.fs.{FileSystem, FileUtil, Path}
import org.apache.hadoop.io.compress.CompressionCodec
import org.apache.spark.sql.DataFrame

// Copied https://stackoverflow.com/a/48712134/6877477
object SparkHelper {

  // This is an implicit class so that saveAsSingleTextFile can be attached to
  // SparkContext and be called like this: sc.saveAsSingleTextFile
  implicit class RDDExtensions(val df: DataFrame) extends AnyVal {

    def saveAsSingleJsonFile(path: String): Unit =
      saveAsSingleJsonFileInternal(path, None)

    def saveAsSingleJsonFile(path: String, codec: Class[_ <: CompressionCodec]): Unit =
      saveAsSingleJsonFileInternal(path, Some(codec))

    private def saveAsSingleJsonFileInternal(path: String, codec: Option[Class[_ <: CompressionCodec]]): Unit = {

      val rdd = df.toJSON.rdd

      // The interface with hdfs:
      val hdfs = FileSystem.get(rdd.sparkContext.hadoopConfiguration)

      // Classic saveAsTextFile in a temporary folder:
      hdfs.delete(new Path(s"$path.tmp"), true) // to make sure it's not there already
      codec match {
        case Some(codec) => rdd.saveAsTextFile(s"$path.tmp", codec)
        case None        => rdd.saveAsTextFile(s"$path.tmp")
      }

      // Merge the folder of resulting part-xxxxx into one file:
      hdfs.delete(new Path(path), true) // to make sure it's not there already
      FileUtil.copyMerge(
        hdfs, new Path(s"$path.tmp"),
        hdfs, new Path(path),
        true, rdd.sparkContext.hadoopConfiguration, null
      )
      // Working with Hadoop 3?: https://stackoverflow.com/a/50545815/9297144

      hdfs.delete(new Path(s"$path.tmp"), true)
    }
  }
}
