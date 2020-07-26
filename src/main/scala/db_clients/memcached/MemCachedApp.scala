package db_clients.memcached

import shade.memcached._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

// docker run --name my-memcache -p 11211:11211 -d memcached

object Write {
  def main(args: Array[String]): Unit = {
    val memcached = Memcached(Configuration("127.0.0.1:11211"))
    println(memcached.set("business:1:name", "Silbo", Duration.Inf))
    println(memcached.set("business:1:stars", 5, Duration.Inf))
  }
}

object Read {
  def main(args: Array[String]): Unit = {
    val memcached = Memcached(Configuration("127.0.0.1:11211"))
    val maybeName = memcached.awaitGet[String]("business:1:name")
    val maybeStars = memcached.awaitGet[Int]("business:1:stars")

    maybeName match {
      case Some(name) => println(s"name = $name")
      case None => println("No Name")
    }

    println(s"start = ${maybeStars.getOrElse(-1)}")
  }
}
