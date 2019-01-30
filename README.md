# headlines
Simple demonstration of using Http4s with Monix Task and Cats IO effect interpreters

## Instructions
Run `sbt run` and select application to run:
* `com.example.headlines.IOMain` for Cats IO
* `com.example.headlines.MonixMain` for Monix Task

## Description
This is a simple project for learning purposes and demonstrates use of [http4s](https://http4s.org/) for a simple microservice that 
scrapes headlines (h2 tags) from [http://www.nytimes.com](http://www.nytimes.com).

Some notes about the implementation:
* Uses Blaze - an NIO backed non-blocking HTTP server that is part of the http4s project
* Uses parameterized effect types i.e. F[_]:Effect.  Thus allowing Cats IO or Monix Task interpretations
* Uses http4s/Blaze http client to retrieve html
* Uses [scala scraper](https://github.com/ruippeixotog/scala-scraper) to parse h2 tags
* Simple demonstration of unit testing http4s `HttpRoutes` using specs2 
