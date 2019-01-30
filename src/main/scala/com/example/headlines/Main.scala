package com.example.headlines

import cats.effect._
import cats.implicits._
import fs2.Stream
import monix.eval.instances.CatsConcurrentEffectForTask
import org.http4s.server.blaze.BlazeServerBuilder

/**
  * Uses Monix Task Effect
  */
object MonixMain extends IOApp {

  def run(args: List[String]) = {
    import monix.execution.Scheduler.Implicits.global
    import monix.eval._
    implicit val taskOptions = Task.defaultOptions
    implicit val monixEffect = new CatsConcurrentEffectForTask()

    BlazeServer.stream[Task].compile.drain.as(ExitCode.Success).toIO
  }
}

/**
  * Uses Cats IO Effect
  */
object IOMain extends IOApp {

  def run(args: List[String]) = {
    BlazeServer.stream[IO].compile.drain.as(ExitCode.Success)
  }

}

object BlazeServer {
  import org.http4s.implicits._

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F]): Stream[F, ExitCode] = {
    val headlinesService = HeadlinesService.nyTimesHeadlinesService[F]
    val routes = HeadlinesRoutes.headlinesRoutes[F](headlinesService).orNotFound
    BlazeServerBuilder[F]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(routes)
      .serve
  }
}