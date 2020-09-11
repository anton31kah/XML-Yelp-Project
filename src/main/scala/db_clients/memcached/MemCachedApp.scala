package db_clients.memcached

import shade.memcached.MemcachedCodecs.AnyRefBinaryCodec
import shade.memcached._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

// https://github.com/docker-library/memcached
// https://stackoverflow.com/questions/19560150/get-all-keys-set-in-memcached
// https://medium.com/@SkyscannerEng/journey-to-the-centre-of-memcached-b239076e678a#2740
// docker run --name memcached1 -p 11211:11211 -d memcached
// docker run --name memcached2 -p 11212:11211 -d memcached

/*
via ubuntu subsystem -> https://www.journaldev.com/16/memcached-telnet-commands-example
tab1:
  $ telnet localhost 11211
tab2:
  $ telnet localhost 11212
  $ get business:1:name
  $ // no quotes
observation: memcached inserts the items using round robin
 */

object Write {
  def main(args: Array[String]): Unit = {
    val memcached = Memcached(Configuration("127.0.0.1:11211 127.0.0.1:11212"))
    println(memcached.set("business:1:name", "Silbo", Duration.Inf))
    println(memcached.set("business:1:stars", 5, Duration.Inf))
  }
}

object Cluster {
  def main(args: Array[String]): Unit = {
    val memcached = Memcached(Configuration("127.0.0.1:11211 127.0.0.1:11212"))

    for (i <- 1 to 1000) {
      memcached.awaitSet(s"a:$i", s"$i-$i", Duration.Inf)
    }

    println("Done")

//    val business = Business(
//      "1",
//      Some("Silbo"),
//      Some("partizanska"),
//      Some("Skopje"),
//      Some("Skopje"),
//      Some("2.2"),
//      Some("5.3"),
//      Some("5"),
//      Some("100"),
//      Some("true"),
//      Some("123, 456, 789"),
//      Some(Hours(Some("12:00-18:00"), None, Some("08:00-16:00"), None, None, None, None))
//    )
//    memcached.set("multi", business, Duration.Inf)

//    val maybeBusiness = memcached.awaitGet[Business]("multi")
//
//    maybeBusiness match {
//      case Some(value) =>
//        println(value)
//      case None => println("None")
//    }
//
//    println("Done")

//    val maybeName = memcached.awaitGet[String]("business:1:name")

//    maybeName match {
//      case Some(name) => println(s"name = $name")
//      case None => println("No Name")
//    }
  }
}

object Read {
  def main(args: Array[String]): Unit = {
    val memcached  = Memcached(Configuration("127.0.0.1:11211 127.0.0.1:11212"))
    val maybeName  = memcached.awaitGet[String]("business:1:name")
    val maybeStars = memcached.awaitGet[Int]("business:1:stars")

    maybeName match {
      case Some(name) => println(s"name = $name")
      case None       => println("No Name")
    }

    println(s"start = ${maybeStars.getOrElse(-1)}")
  }
}
