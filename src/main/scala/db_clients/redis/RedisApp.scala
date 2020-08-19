package db_clients.redis

import redis.clients.jedis.{HostAndPort, JedisCluster}

import scala.collection.JavaConverters._

//import com.redis._
//import com.redis.cluster.{ClusterNode, KeyTag, RedisCluster}
//import com.redis.serialization.Parse.Implicits._

// docker run --name some-redis -p 6379:6379 -d redis
// to access redis-cli
// docker exec -it some-redis /bin/bash
// and when you get in, run
// $ redis-cli

object Model1 {
  case class Business(
      id: Int,
      name: String,
      address: String,
      city: String,
      state: String,
      latitude: Double,
      longitude: Double,
      stars: Int,
      review_count: Int,
      is_open: Boolean,
      categories: Vector[String],
      hours: Map[String, String],
      reviews: Vector[Int]
  )

  case class Review(
      id: Int,
      user_id: Int,
      business_id: Int,
      stars: Int,
      date: String,
      text: String,
      useful: Int
  )

  case class User(
      id: Int,
      name: String,
      review_count: Int,
      yelping_since: String,
      friends: Vector[Int],
      compliments: Int,
      average_stars: Double,
      reviews: Vector[Int]
  )
}

object Model2 {
  case class Business(
      id: Int,
      name: String,
      address: String,
      city: String,
      state: String,
      latitude: Double,
      longitude: Double,
      stars: Int,
      review_count: Int,
      is_open: Boolean,
      categories: Vector[String],
      hours: Map[String, String],
      reviews: Vector[Review]
  )

  case class Review(
      id: Int,
      user_id: Int,
      stars: Int,
      date: String,
      text: String,
      useful: Int
  )

  case class User(
      id: Int,
      name: String,
      review_count: Int,
      yelping_since: String,
      friends: Vector[Int],
      compliments: Int,
      average_stars: Double,
      reviews: Map[Int, Int]
  )
}

object SampleData {
  object Model1Data {
    val businesses = Vector(
      Model1.Business(
        1,
        "Silbo",
        "Partizanska",
        "Skopje",
        "Skopje",
        1.1,
        2.2,
        5,
        2,
        true,
        Vector("kolbasi", "burek", "gevrek", "blago"),
        Map("Monday" -> "00:00-24:00", "Wednesday" -> "00:00-24:00"),
        Vector(1, 2)
      ),
      Model1.Business(
        2,
        "Silbo2",
        "Aerodrom",
        "Skopje",
        "Skopje",
        1.1,
        2.2,
        5,
        2,
        false,
        Vector("kolbasi", "burek", "gevrek", "blago"),
        Map("Monday" -> "00:00-24:00", "Wednesday" -> "00:00-24:00"),
        Vector(3)
      )
    )

    val reviews = Vector(
      Model1.Review(1, 1, 1, 5, "10/8/2020", "Mnogu vkusno", 10),
      Model1.Review(2, 2, 1, 5, "11/8/2020", "Mnogu dobro", 10),
      Model1.Review(3, 3, 2, 5, "12/8/2020", "Mnogu loso", 10)
    )

    val users = Vector(
      Model1
        .User(1, "Anton", 1, "10/10/2018", Vector(2, 3), 10, 4.5, Vector(1)),
      Model1
        .User(2, "Ljupcho", 1, "10/10/2017", Vector(1, 3), 10, 4.5, Vector(2)),
      Model1
        .User(3, "Nikola", 1, "10/10/2016", Vector(1, 2), 10, 4.5, Vector(3))
    )
  }
}

object ClusterTest {
  def main(args: Array[String]): Unit = {
    val cluster = new JedisCluster(
      Set(
        new HostAndPort("15.161.125.18", 6379),
        new HostAndPort("15.161.125.18", 6479),
        new HostAndPort("15.161.90.71", 6379),
        new HostAndPort("15.161.90.71", 6479),
        new HostAndPort("15.161.95.163", 6379),
        new HostAndPort("15.161.95.163", 6479)
      ).asJava
    )
    val result = cluster.get("c")
    println(s"get result = $result")
  }
}

object WriteSampleData {
  def main(args: Array[String]): Unit = {
    val cluster = new JedisCluster(
      Set(
        new HostAndPort("15.161.125.18", 6379),
        new HostAndPort("15.161.125.18", 6479),
        new HostAndPort("15.161.90.71", 6379),
        new HostAndPort("15.161.90.71", 6479),
        new HostAndPort("15.161.95.163", 6379),
        new HostAndPort("15.161.95.163", 6479)
      ).asJava
    )

    for (business <- SampleData.Model1Data.businesses) {
      cluster.hmset(
        s"business:${business.id}",
        Map(
          "name" -> business.name,
          "address" -> business.address,
          "city" -> business.city,
          "state" -> business.state,
          "latitude" -> business.latitude,
          "longitude" -> business.longitude,
          "stars" -> business.stars,
          "review_count" -> business.review_count,
          "is_open" -> business.is_open
        ).mapValues(_.toString).asJava
      )

      cluster.lpush(s"business:${business.id}:categories", business.categories: _*)

      cluster.hmset(s"business:${business.id}:hours", business.hours.asJava)
    }

    for (user <- SampleData.Model1Data.users) {
      cluster.hmset(
        s"user:${user.id}",
        Map(
          "name"          -> user.name,
          "review_count"  -> user.review_count,
          "yelping_since" -> user.yelping_since,
          "compliments"   -> user.compliments,
          "average_stars" -> user.average_stars
        ).mapValues(_.toString).asJava
      )

      cluster.lpush(s"user:${user.id}:friends", user.friends.map(_.toString): _*)
    }

    for (review <- SampleData.Model1Data.reviews) {
      cluster.hmset(
        s"review:${review.id}",
        Map(
          "user_id"     -> review.user_id,
          "business_id" -> review.business_id,
          "stars"       -> review.stars,
          "date"        -> review.date,
          "text"        -> review.text,
          "useful"      -> review.useful
        ).mapValues(_.toString).asJava
      )

      cluster.lpush(s"business:${review.business_id}:reviews", review.id.toString)
      cluster.lpush(s"user:${review.user_id}:reviews", review.id.toString)
    }

    cluster.hset("TEST", "X", 1.toString)
    cluster.hset("TEST", "Y", 2.toString)
    val result = cluster.hgetAll("TEST") // returns Map(X -> 1, Y -> 2)
  }
}

//object Write {
//  def main(args: Array[String]): Unit = {
//    val redisClient = new RedisClient("localhost", 6379)
//    redisClient.set("business:1:name", "Silbo")
//    redisClient.set("business:1:stars", 5)
//  }
//}
//
//object Remove {
//  def main(args: Array[String]): Unit = {
//    val redisClient = new RedisClient("localhost", 6379)
//    redisClient.flushall
//  }
//}
//
//object Read {
//  def main(args: Array[String]): Unit = {
//    val redisClient = new RedisClient("localhost", 6379)
//    val maybeName = redisClient.get[String]("business:1:name")
//    val maybeStars = redisClient.get[Int]("business:1:stars")
//
//    maybeName match {
//      case Some(name) => println(s"name = $name")
//      case None => println("No Name")
//    }
//
//    println(s"start = ${maybeStars.getOrElse(-1)}")
//  }
//}
