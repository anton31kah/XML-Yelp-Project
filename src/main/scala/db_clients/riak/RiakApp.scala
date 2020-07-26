package db_clients.riak

import com.basho.riak.client.api.RiakClient
import com.basho.riak.client.api.commands.kv.{FetchValue, StoreValue}
import com.basho.riak.client.core.query.{Location, Namespace}

// docker-compose up --scale member=1

object Write {
  def main(args: Array[String]): Unit = {
    val riakClient = RiakClient.newClient()

    val businessBucket = new Namespace("BusinessBucket")

    val nameLocation = new Location(businessBucket, "business:1:name")
    val nameStore = new StoreValue.Builder("Silbo").withLocation(nameLocation).build()
    riakClient.execute(nameStore)

    val starsLocation = new Location(businessBucket, "business:1:stars")
    val starsStore = new StoreValue.Builder(5).withLocation(starsLocation).build()
    riakClient.execute(starsStore)

    riakClient.shutdown()
  }
}

object Read {
  def main(args: Array[String]): Unit = {
    val riakClient = RiakClient.newClient()

    val businessBucket = new Namespace("BusinessBucket")

    val nameLocation = new Location(businessBucket, "business:1:name")
    val nameFetch = new FetchValue.Builder(nameLocation).build()
    val nameResponse = riakClient.execute(nameFetch)
    val maybeName = Option(nameResponse.getValue[String](classOf[String]))

    maybeName match {
      case Some(name) => println(s"name = $name")
      case None => println("No Name")
    }

    val starsLocation = new Location(businessBucket, "business:1:stars")
    val starsFetch = new FetchValue.Builder(starsLocation).build()
    val starsResponse = riakClient.execute(starsFetch)
    val maybeStars = Option(starsResponse.getValue[Int](classOf[Int]))

    println(s"start = ${maybeStars.getOrElse(-1)}")

    riakClient.shutdown()
  }
}
