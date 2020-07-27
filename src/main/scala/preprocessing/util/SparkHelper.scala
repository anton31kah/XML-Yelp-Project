package preprocessing.util

import java.io.IOException

import scala.util.Try
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.compress.CompressionCodec
import org.apache.hadoop.io.IOUtils
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
      LegacyFileUtil.copyMerge(
        hdfs, new Path(s"$path.tmp"),
        hdfs, new Path(path),
        deleteSource = true, rdd.sparkContext.hadoopConfiguration
      )
      // Working with Hadoop 3?: https://stackoverflow.com/a/50545815/9297144

      hdfs.delete(new Path(s"$path.tmp"), true)
    }
  }

  // https://stackoverflow.com/a/50545815/6877477
  private object LegacyFileUtil {
    def copyMerge(
                   srcFS: FileSystem, srcDir: Path,
                   dstFS: FileSystem, dstFile: Path,
                   deleteSource: Boolean, conf: Configuration
                 ): Boolean = {

      if (dstFS.exists(dstFile))
        throw new IOException(s"Target $dstFile already exists")

      // Source path is expected to be a directory:
      if (srcFS.getFileStatus(srcDir).isDirectory) {

        val outputFile = dstFS.create(dstFile)
        Try {
          srcFS
            .listStatus(srcDir)
            .sortBy(_.getPath.getName)
            .collect {
              case status if status.isFile =>
                val inputFile = srcFS.open(status.getPath)
                Try(IOUtils.copyBytes(inputFile, outputFile, conf, false))
                inputFile.close()
            }
        }
        outputFile.close()

        if (deleteSource) srcFS.delete(srcDir, true) else true
      }
      else false
    }
  }
}
