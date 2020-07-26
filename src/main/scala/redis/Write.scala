package redis

import com.redis._

object Write {
  def main(args: Array[String]): Unit = {
    // docker run --name some-redis -p 6379:6379 -d redis
    val redisClient = new RedisClient("localhost", 6379)
    println(redisClient.set("business:1:name", "Silbo"))
    println(redisClient.set("business:1:stars", 5))
  }
}
