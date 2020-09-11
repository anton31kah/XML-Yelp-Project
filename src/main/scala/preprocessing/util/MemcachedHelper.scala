package preprocessing.util

import java.net.InetSocketAddress
import java.nio.file.{Files, Paths => JavaPaths}
import java.time.Instant
import java.util
import java.util.Properties

import net.spy.memcached.MemcachedClient
import shade.memcached.{Configuration, Memcached}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global

object MemcachedHelper {

  def createCluster(implicit properties: Map[String, String]): Memcached = {
    def lookup(props: Map[String, String], key: String) = props.find(_._1 endsWith key).map(_._2)

    Memcached(Configuration(
      properties
        .filter(_._1 startsWith "memcached.cluster")
        .groupBy(_._1.split('.')(2))
        .values
        .map(p => lookup(p, "host") -> lookup(p, "port"))
        .flatMap(p => for {host <- p._1; port <- p._2} yield s"$host:${port.toInt}")
        .mkString(" ")
    ))
  }

  def createClient(implicit properties: Map[String, String]): MemcachedClient = {
    def lookup(props: Map[String, String], key: String) = props.find(_._1 endsWith key).map(_._2)

    new MemcachedClient(
      properties
        .filter(_._1 startsWith "memcached.cluster")
        .groupBy(_._1.split('.')(2))
        .values
        .map(p => lookup(p, "host") -> lookup(p, "port"))
        .flatMap(p => for {host <- p._1; port <- p._2} yield new InetSocketAddress(host, port.toInt))
        .toVector.asJava
    )
  }

  def serialize(map: Map[String, String]): util.HashMap[String, String] = new util.HashMap[String, String](map.asJava)

  def log(message: String): Unit = println(s"$message at ${Instant.now()}")

  def readProperties: Map[String, String] = {
    val properties = new Properties()
    properties.load(Files.newBufferedReader(JavaPaths.get(Paths.properties)))
    properties.asScala.toMap
  }
}
