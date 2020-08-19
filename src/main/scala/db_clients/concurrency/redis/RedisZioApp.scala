package db_clients.concurrency.redis

//import java.util.concurrent.TimeUnit
//
//import com.redis.{RedisClient, RedisClientPool}
//import db_clients.concurrency.SampleData
//import zio._
//
//import scala.concurrent.Future
//
//object RedisZioApp extends App {
//  import zio.clock._
//  import zio.console._
//
//  val redisClients = new RedisClientPool("localhost", 6379)
//
//  def set(pair: (String, Any))(implicit client: RedisClient): Boolean =
//    client.set(pair._1, pair._2)
//
//  def delAll(implicit client: RedisClient): Long =
//    client
//      .keys()
//      .map(_.flatten)
//      .filterNot(_.isEmpty)
//      .flatMap(keys => client.del(keys.head, keys = keys.tail: _*))
//      .getOrElse(0)
//
//  def asEffect[T](clientOp: RedisClient => T): Task[T] =
//    ZIO.fromFuture(implicit ex => Future(redisClients.withClient(clientOp)))
//
//  def putPair[T](pair: (String, Any)): Task[Boolean] =
//    asEffect(implicit client => set(pair))
//
//  def putMap[T](): Task[List[Boolean]] =
//    ZIO.foreachPar(SampleData.business)(putPair)
//
//  def clearDb(): Task[Long] =
//    asEffect(implicit client => delAll)
//
//  val logic: ZIO[Console with Clock, Throwable, Unit] = for {
//    _            <- putStrLn("First we clear the db")
//    removedPairs <- clearDb()
//    _            <- putStrLn(s"Cleared $removedPairs")
//    _            <- putStrLn("Start")
//    startTime    <- currentTime(TimeUnit.MILLISECONDS)
//    _            <- putMap()
//    endTime      <- currentTime(TimeUnit.MILLISECONDS)
//    _            <- putStrLn("Done")
//    _            <- putStrLn(s"Started at $startTime and finished at $endTime")
//    _            <- putStrLn(s"It took ${endTime - startTime} milliseconds") // nearly 1 second
//  } yield ()
//
//  override def run(args: List[String]): URIO[ZEnv, ExitCode] = logic.exitCode
//}
