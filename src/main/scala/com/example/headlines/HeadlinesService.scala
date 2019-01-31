package com.example.headlines

import cats.effect.Effect
import cats.implicits._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList
import org.http4s.Uri
import org.http4s.client.Client

trait HeadlinesService[F[_]] {
  def scrape():F[List[Headline]]
}

final case class Headline(name:String)

/**
  * Calls nytimes.com to retrieve home page HTML for NY Times. Parses all h2 tags
  * as Headline instances
  *
  * @param httpClient
  * @param effect$F
  * @tparam F
  */
class NyTimesHeadlinesService[F[_]:Effect](httpClient:Client[F]) extends HeadlinesService[F] {

  val browser = JsoupBrowser()
  val uri = Uri.uri("http://nytimes.com")

  override def scrape(): F[List[Headline]] = httpClient
    .expect[String]("http://nytimes.com")
    .map(html => {
      (browser.parseString(html) >> elementList("h2"))
        .map(_.text)
        .distinct.sorted
        .map(Headline)
    })
}
