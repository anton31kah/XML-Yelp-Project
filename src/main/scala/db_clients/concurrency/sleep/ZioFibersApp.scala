package db_clients.concurrency.sleep

//import java.util.concurrent.TimeUnit
//
//import zio._
//
//import scala.concurrent.Future
//
//object ZioFibersApp extends App {
//
//  import zio.clock._
//  import zio.console._
//
//  val tasksIterator: Range.Inclusive = 1 to 100
//
//  def wait1Second(): UIO[Fiber.Runtime[Throwable, Unit]] =
//    ZIO.fromFuture(implicit ex => Future(Thread.sleep(1000))).fork
//
//  def executePar(): UIO[Unit] =
//    ZIO.mergeAllPar(tasksIterator.map(_ => wait1Second()))(())((x, _) => x)
//
//  def executeSync(): UIO[Unit] =
//    ZIO.mergeAll(tasksIterator.map(_ => wait1Second()))(())((x, _) => x)
//
//  val logic: ZIO[Console with Clock, Throwable, Unit] = for {
//    _         <- putStrLn("Start")
//    startTime <- currentTime(TimeUnit.MILLISECONDS)
////    _         <- executePar() // 800 milliseconds
//    _         <- executeSync() // seconds
//    endTime   <- currentTime(TimeUnit.MILLISECONDS)
//    _         <- putStrLn("Done")
//    _         <- putStrLn(s"Started at $startTime and finished at $endTime")
//    _         <- putStrLn(s"It took ${endTime - startTime} milliseconds")
//  } yield ()
//
//  override def run(args: List[String]): URIO[ZEnv, ExitCode] = logic.exitCode
//}
