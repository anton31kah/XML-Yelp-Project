package preprocessing.util

import java.nio.file.{Files, Paths => JavaPaths}
import java.time.Instant
import java.util.Properties

import redis.clients.jedis.{HostAndPort, JedisCluster}

import scala.collection.JavaConverters._

object JedisHelper {

  def createCluster(implicit properties: Map[String, String]): JedisCluster = {
    def lookup(props: Map[String, String], key: String) = props.find(_._1 endsWith key).map(_._2)

    new JedisCluster(
      properties
        .filter(_._1 startsWith "redis.cluster")
        .groupBy(_._1.split('.')(2))
        .values
        .map(p => lookup(p, "host") -> lookup(p, "port"))
        .flatMap(p => for { host <- p._1; port <- p._2 } yield new HostAndPort(host, port.toInt))
        .toSet.asJava
    )
  }

  def log(message: String): Unit = println(s"$message at ${Instant.now()}")

  def readProperties: Map[String, String] = {
    val properties = new Properties()
    properties.load(Files.newBufferedReader(JavaPaths.get(Paths.properties)))
    properties.asScala.toMap
  }
}
