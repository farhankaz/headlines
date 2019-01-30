package com.example.headlines

import cats.effect.Effect
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object HeadlinesRoutes {

  def headlinesRoutes[F[_]: Effect](headlinesService: HeadlinesService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root =>
        Ok(
          // makes effectful call to retrieve headlines and streams them as strings
          Stream.eval(headlinesService.scrape())
            .flatMap(Stream.emits(_))
            .map(_.name)
            .intersperse("\n")
        )
    }
  }

}