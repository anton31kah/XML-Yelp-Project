package preprocessing.jobs

import java.nio.file.{Files, Paths => JavaPaths}
import java.time.Instant
import java.util.Properties

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{to_date, year}
import preprocessing.models.json.{Business, Review, User}
import preprocessing.util.Paths
import redis.clients.jedis.{HostAndPort, JedisCluster}

import scala.collection.JavaConverters._

object InsertingModel1Redis {
  def main(args: Array[String]): Unit = {
    println(s"before spark session create at ${Instant.now()}")

    val propertiesJava = new Properties()
    propertiesJava.load(Files.newBufferedReader(JavaPaths.get(Paths.properties)))
    val properties = propertiesJava.asScala

    val sparkSession = SparkSession.builder
      .appName("Inserting Model1 Redis App")
      .master(properties("spark.url"))
      .getOrCreate()

    import sparkSession.implicits._

    println(s"spark session created at ${Instant.now()}")

    val minYear = 2019

    val businessDf = sparkSession.read.json(Paths.RealData.business)
      .drop("attributes", "postal_code")
      .as[Business]
      .filter(_.business_id != null)

    val reviewDf = sparkSession.read.json(Paths.RealData.review)
      .filter(year(to_date($"date", "yyyy-MM-dd HH:mm:ss")) >= minYear)
      .drop("funny", "cool")
      .as[Review]
      .filter(_.review_id != null)

    val userDf = sparkSession.read.json(Paths.RealData.user)
      .filter(year(to_date($"yelping_since", "yyyy-MM-dd HH:mm:ss")) >= minYear)
      .drop("useful", "funny", "cool", "elite", "fans")
      .as[User]
      .filter(_.user_id != null)

    println(s"dataframe created at ${Instant.now()}")

    println(s"before business foreachPartition at ${Instant.now()}")

    businessDf.foreachPartition((rows: Iterator[Business]) => {
      println(s"before business cluster at ${Instant.now()}")

      val cluster = new JedisCluster(
        Set(
          new HostAndPort(properties("redis.cluster.master1.host"), properties("redis.cluster.master1.port").toInt),
          new HostAndPort(properties("redis.cluster.slave1.host"), properties("redis.cluster.slave1.port").toInt),
          new HostAndPort(properties("redis.cluster.master2.host"), properties("redis.cluster.master2.port").toInt),
          new HostAndPort(properties("redis.cluster.slave2.host"), properties("redis.cluster.slave2.port").toInt),
          new HostAndPort(properties("redis.cluster.master3.host"), properties("redis.cluster.master3.port").toInt),
          new HostAndPort(properties("redis.cluster.slave3.host"), properties("redis.cluster.slave3.port").toInt)
        ).asJava
      )

      println(s"before business foreach at ${Instant.now()}")

      rows.foreach(business => {
        cluster.hmset(s"business:${business.business_id}", Map(
          "name" -> business.name,
          "address" -> business.address,
          "city" -> business.city,
          "state" -> business.state,
          "latitude" -> business.latitude,
          "longitude" -> business.longitude,
          "stars" -> business.stars,
          "review_count" -> business.review_count,
          "is_open" -> business.is_open
        ).filter(_._2.isDefined).mapValues(_.get).asJava)

        if (business.categories.isDefined) {
          cluster.lpush(
            s"business:${business.business_id}:categories",
            business.categories.get.split(", "): _*
          )
        }

        if (business.hours.isDefined) {
          cluster.hmset(
            s"business:${business.business_id}:hours",
            business.hours.get.toMap.asJava
          )
        }
      })

      println(s"after business foreach at ${Instant.now()}")
    })

    println(s"after business foreachPartition at ${Instant.now()}")

    println(s"before user foreachPartition at ${Instant.now()}")

    userDf.foreachPartition((rows: Iterator[User]) => {
      println(s"before user cluster at ${Instant.now()}")

      val cluster = new JedisCluster(
        Set(
          new HostAndPort(properties("redis.cluster.master1.host"), properties("redis.cluster.master1.port").toInt),
          new HostAndPort(properties("redis.cluster.slave1.host"), properties("redis.cluster.slave1.port").toInt),
          new HostAndPort(properties("redis.cluster.master2.host"), properties("redis.cluster.master2.port").toInt),
          new HostAndPort(properties("redis.cluster.slave2.host"), properties("redis.cluster.slave2.port").toInt),
          new HostAndPort(properties("redis.cluster.master3.host"), properties("redis.cluster.master3.port").toInt),
          new HostAndPort(properties("redis.cluster.slave3.host"), properties("redis.cluster.slave3.port").toInt)
        ).asJava
      )

      println(s"before user foreach at ${Instant.now()}")

      rows.foreach(user => {
        cluster.hmset(s"user:${user.user_id}", Map(
          "name" -> user.name,
          "review_count" -> user.review_count,
          "yelping_since" -> user.yelping_since,
          "friends" -> user.friends,
          "average_stars" -> user.average_stars,
          "compliments" -> Some(user.compliments.toString)
        ).filter(_._2.isDefined).mapValues(_.get).asJava)

        if (user.friends.isDefined) {
          cluster.lpush(
            s"user:${user.user_id}:friends",
            user.friends.get.split(", "): _*
          )
        }
      })

      println(s"after user foreach at ${Instant.now()}")
    })

    println(s"after user foreachPartition at ${Instant.now()}")

    println(s"before review foreachPartition at ${Instant.now()}")

    reviewDf.foreachPartition((rows: Iterator[Review]) => {
      println(s"before review cluster at ${Instant.now()}")

      val cluster = new JedisCluster(
        Set(
          new HostAndPort(properties("redis.cluster.master1.host"), properties("redis.cluster.master1.port").toInt),
          new HostAndPort(properties("redis.cluster.slave1.host"), properties("redis.cluster.slave1.port").toInt),
          new HostAndPort(properties("redis.cluster.master2.host"), properties("redis.cluster.master2.port").toInt),
          new HostAndPort(properties("redis.cluster.slave2.host"), properties("redis.cluster.slave2.port").toInt),
          new HostAndPort(properties("redis.cluster.master3.host"), properties("redis.cluster.master3.port").toInt),
          new HostAndPort(properties("redis.cluster.slave3.host"), properties("redis.cluster.slave3.port").toInt)
        ).asJava
      )

      println(s"before review foreach at ${Instant.now()}")

      rows.foreach(review => {
        cluster.hmset(s"review:${review.review_id}", Map(
          "user_id" -> Some(review.user_id),
          "business_id" -> Some(review.business_id),
          "stars" -> review.stars,
          "date" -> review.date,
          "text" -> review.text,
          "useful" -> review.useful
        ).filter(_._2.isDefined).mapValues(_.get).asJava)

        cluster.lpush(s"business:${review.business_id}:reviews", review.review_id)
        cluster.lpush(s"user:${review.user_id}:reviews", review.review_id)
      })

      println(s"after review foreach at ${Instant.now()}")
    })

    println(s"after review foreachPartition at ${Instant.now()}")

    println(s"done with inserting dataframes at ${Instant.now()}")

    println(s"will stop spark session at ${Instant.now()}")

    sparkSession.stop()

    println(s"stopped spark session at ${Instant.now()}")
  }
}
