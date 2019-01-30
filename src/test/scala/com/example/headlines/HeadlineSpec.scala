package com.example.headlines

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import cats.implicits._
import com.example.headlines.HeadlinesService.Headline

class HeadlineSpec extends org.specs2.mutable.Specification {

  "Headlines" >> {
    "return 200" >> {
      uriReturns200()
    }
    "return headlines" >> {
      uriReturnsHeadlines()
    }
  }

  private[this] val retHeadlines: Response[IO] = {
    val getHW = Request[IO](Method.GET, Uri.uri("/"))
    val headlines = new HeadlinesService[IO] {
      override def scrape(): IO[List[HeadlinesService.Headline]] = List(
        Headline("test headline1"),
        Headline("test headline2"),
      ).pure[IO]
    }
    HeadlinesRoutes.headlinesRoutes(headlines).orNotFound(getHW).unsafeRunSync()
  }

  private[this] def uriReturns200(): MatchResult[Status] =
    retHeadlines.status must beEqualTo(Status.Ok)

  private[this] def uriReturnsHeadlines(): MatchResult[String] =
    retHeadlines.as[String].unsafeRunSync() must beEqualTo("test headline1\ntest headline2\n")
}