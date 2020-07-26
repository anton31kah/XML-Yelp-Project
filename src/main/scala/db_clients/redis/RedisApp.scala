package db_clients.redis

import com.redis._
import com.redis.serialization.Parse.Implicits._

// docker run --name some-redis -p 6379:6379 -d redis

object Write {
  def main(args: Array[String]): Unit = {
    val redisClient = new RedisClient("localhost", 6379)
    redisClient.set("business:1:name", "Silbo")
    redisClient.set("business:1:stars", 5)
  }
}

object Read {
  def main(args: Array[String]): Unit = {
    val redisClient = new RedisClient("localhost", 6379)
    val maybeName = redisClient.get[String]("business:1:name")
    val maybeStars = redisClient.get[Int]("business:1:stars")

    maybeName match {
      case Some(name) => println(s"name = $name")
      case None => println("No Name")
    }

    println(s"start = ${maybeStars.getOrElse(-1)}")
  }
}
