package db_clients.concurrency

import com.redis.RedisClient

object RedisApp {
  def main(args: Array[String]): Unit = {
    val redisClient = new RedisClient("localhost", 6379)

    println("First we clear the db")

    val removedPairs = redisClient
      .keys()
      .map(_.flatten)
      .filterNot(_.isEmpty)
      .flatMap(keys => redisClient.del(keys.head, keys = keys.tail: _*))
      .getOrElse(0)

    println(s"Cleared $removedPairs")

    println("Start")

    val startTime = System.currentTimeMillis()

    SampleData.business.foreach(pair => redisClient.set(pair._1, pair._2))

    val endTime = System.currentTimeMillis()

    println("Done")
    println(s"Started at $startTime and finished at $endTime")
    println(s"It took ${endTime - startTime} milliseconds")
  }
}
