package com.example.headlines

import cats.effect._
import cats.implicits._
import fs2.Stream
import monix.eval.instances.CatsConcurrentEffectForTask
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.middleware.FollowRedirect
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

/**
  * Uses Monix Task Effect
  */
object MonixMain extends IOApp {

  def run(args: List[String]) = {
    import monix.eval._
    import monix.execution.Scheduler.Implicits.global
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
    for {
      client <- BlazeClientBuilder[F](global).stream
      clientWithMiddleware = FollowRedirect(3)(client)
      headlinesService = new NyTimesHeadlinesService[F](clientWithMiddleware)
      routes = HeadlinesRoutes.headlinesRoutes[F](headlinesService).orNotFound
      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(routes)
        .serve
    } yield exitCode
  }
}