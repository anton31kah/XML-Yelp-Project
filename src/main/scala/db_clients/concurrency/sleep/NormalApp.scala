package db_clients.concurrency.sleep

//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.duration.Duration
//import scala.concurrent.{Await, Future}
//
//object NormalApp {
//
//  val tasksIterator: Range.Inclusive = 1 to 100
//
//  def wait1Second(dummy: Int): Future[Unit] =
//    Future {
//      for (_ <- 1 to 10) {
//        Thread.sleep(100)
//      }
//    }
//
//  def executePar(): Vector[Unit] =
//    Await.result(
//      Future.traverse(tasksIterator.toVector)(wait1Second),
//      Duration.Inf
//    )
//
//  def executeSync(): Unit =
//    Await.result(Future.sequence(tasksIterator.map(wait1Second)), Duration.Inf)
//
//  def main(args: Array[String]): Unit = {
//    println("Start")
//    val startTime = System.currentTimeMillis()
//
//    executePar() // 13 seconds
////    executeSync() // 13 seconds
//
//    val endTime = System.currentTimeMillis()
//    println("Done")
//
//    println(s"Started at $startTime and finished at $endTime")
//    println(s"It took ${endTime - startTime} milliseconds")
//  }
//}
