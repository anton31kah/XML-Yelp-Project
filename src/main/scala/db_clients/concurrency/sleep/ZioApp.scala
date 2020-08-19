package db_clients.concurrency.sleep

//import java.util.concurrent.TimeUnit
//
//import zio._
//
//import scala.concurrent.Future
//
//object ZioApp extends App {
//
//  import zio.clock._
//  import zio.console._
//
//  val tasksIterator: Range.Inclusive = 1 to 100
//
//  def wait1Second(): Task[Unit] =
//    ZIO.fromFuture(implicit ex => Future(Thread.sleep(1000)))
//
//  def executePar(): Task[List[Unit]] =
//    ZIO.foreachPar(tasksIterator)(_ => wait1Second())
//
//  def executeSync(): Task[List[Unit]] =
//    ZIO.foreach(tasksIterator)(_ => wait1Second())
//
//  val logic: ZIO[Console with Clock, Throwable, Unit] = for {
//    _         <- putStrLn("Start")
//    startTime <- currentTime(TimeUnit.MILLISECONDS)
//    _         <- executePar() // nearly 8 seconds
////    _         <- executeSync() // 100 seconds
//    endTime   <- currentTime(TimeUnit.MILLISECONDS)
//    _         <- putStrLn("Done")
//    _         <- putStrLn(s"Started at $startTime and finished at $endTime")
//    _         <- putStrLn(s"It took ${endTime - startTime} milliseconds")
//  } yield ()
//
//  override def run(args: List[String]): URIO[ZEnv, ExitCode] = logic.exitCode
//}
