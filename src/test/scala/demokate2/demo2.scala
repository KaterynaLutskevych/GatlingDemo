package demokate2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class demo2 extends Simulation {

	val httpProtocol = http
		.baseUrl("https://demo.nopcommerce.com")
		.acceptHeader("image/webp,*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:81.0) Gecko/20100101 Firefox/81.0")
		.disableFollowRedirect

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map("Accept" -> "text/css,*/*;q=0.1")

	val headers_17 = Map("Accept" -> "*/*")

	val headers_25 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/x-www-form-urlencoded; charset=UTF-8",
		"Origin" -> "https://demo.nopcommerce.com",
		"X-Requested-With" -> "XMLHttpRequest")



	val scn = scenario("demo2")
		.exec(http("Browse page")
			.get("/")
			.check(regex("<title>(.+?)</title>").is("nopCommerce demo store"))
			.headers(headers_0))
		.exec(http("Search product by key")
				.get("/search?q=laptop")
			  .check(status.is(200))
				.headers(headers_0))
				.pause(3)
		.exec(http("Open product page")
			  .get("/asus-n551jk-xo076h-laptop").check(substring("Asus"))
			  .check(regex("<h1>(.+?)</h1>").is("Asus N551JK-XO076H Laptop"))
			  .headers(headers_0))
		.exec(http("Add to cart")
			  .post("/addproducttocart/details/5/1")
			  .headers(headers_25))
		.exec(http("Cart")
			  .get("/cart")
			  .check(status.is(200))
			  .headers(headers_0))

	  setUp(scn.inject(atOnceUsers(1))).assertions(global.successfulRequests.percent.gt(99)).protocols(httpProtocol)
}