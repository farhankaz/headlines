package com.example.headlines

import cats.effect.Effect
import cats.implicits._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext.global

trait HeadlinesService[F[_]] {
  def scrape():F[List[HeadlinesService.Headline]]
}

object HeadlinesService {
  final case class Headline(name:String)

  def nyTimesHeadlinesService[F[_]](implicit F:Effect[F]): HeadlinesService[F] = new HeadlinesService[F]{
    val browser = JsoupBrowser()
    def scrape(): F[List[HeadlinesService.Headline]] = {
      F.delay {
        (browser.get("http://nytimes.com") >> elementList("h2"))
          .map(_.text)
          .distinct.sorted
          .map(Headline)
      }
    }
  }
}
