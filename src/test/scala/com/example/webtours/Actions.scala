package com.example.webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Actions {

  val userFeeder = csv("users.csv").circular


  def getMainPage = exec(
    http("mainPage")
      .get("webtours/")
      .check(status.is(200))
  )

  def getUserSession = exec(
    http("getUserSession")
      .get("cgi-bin/nav.pl?in=home")
      .check(status.is(200))
      .check(regex("""<input type="hidden" name="userSession" value="(.+?)"/>""").saveAs("userSession")) // Извлекаем значение
  )

  def login = feed(userFeeder).exec(
    http("login")
      .get("cgi-bin/login.pl")
      .check(status.is(200))
      .formParam("userSession", "#{userSession}")
      .formParam("username", "#{username}")
      .formParam("password", "#{password}")
  )

  def getFlightsPage = exec(
    http("flightsPage")
      .get("cgi-bin/nav.pl?page=menu&in=flights")
      .check(status.is(200))
  )


  def getSities = exec(
    http("Get Cities List")
      .get("/cgi-bin/reservations.pl?page=welcome")
      .check(regex("""<option value="([^"]+)">""").findRandom.saveAs("citiesFrom"))
      .check(regex("""<option value="([^"]+)">""").findRandom.saveAs("citiesTo"))
  )


  def reservationsFindFlight = exec { session =>
    // Генерация случайных дат при выполнении данного действия
    val (departDate, returnDate) = RandomDate.generateDates()

    // Сохраняем случайные даты в сессии
    session
      .set("departDate", departDate) // Сохраняем дату отправления
      .set("returnDate", returnDate) // Сохраняем дату возвращения
  }
    .exec(
      http("Find Flights")
        .post("http://webtours.load-test.ru:1080/cgi-bin/reservations.pl")
        .headers(Map(
          "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
          "Accept-Language" -> "ru-RU,ru;q=0.9,en-RU;q=0.8,en;q=0.7,en-US;q=0.6",
          "Cache-Control" -> "no-cache",
          "Connection" -> "keep-alive",
          "Content-Type" -> "application/x-www-form-urlencoded",
          "Origin" -> "http://webtours.load-test.ru:1080",
          "Pragma" -> "no-cache",
          "Referer" -> "http://webtours.load-test.ru:1080/cgi-bin/reservations.pl?page=welcome",
          "Upgrade-Insecure-Requests" -> "1",
          "User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36"
        ))
        .formParam("advanceDiscount", "0")
        .formParam("depart", "#{citiesFrom}")
        .formParam("departDate", "#{departDate}")
        .formParam("arrive", "#{citiesTo}")
        .formParam("returnDate", "#{returnDate}")
        .formParam("numPassengers", "1")
        .formParam("seatPref", "None")
        .formParam("seatType", "Coach")
        .formParam("findFlights.x", "48")
        .formParam("findFlights.y", "9")
        .formParam(".cgifields", "roundtrip")
        .formParam(".cgifields", "seatType")
        .formParam(".cgifields", "seatPref")
        .check(status.is(200))
        .check(css("input[name=\"outboundFlight\"]", "value").findRandom.saveAs("outboundFlight"))
        .check(bodyString.saveAs("responseBody")) // Логируем ответ
    )
//    .exec { session =>
  //      println("Response body: " + session("responseBody").as[String])
  //      session
  //    }

  def reservationsPaymentDetails = exec(
    http("reservationsPaymentDetails")
      .post("cgi-bin/reservations.pl")
      .check(status.is(200))
      .formParam("outboundFlight", "#{outboundFlight}")
  )

  def invoice = exec(
    http("Invoice")
      .post("cgi-bin/reservations.pl")
      .check(status.is(200))
      .formParam("outboundFlight", "#{outboundFlight}")
      .formParam("firstName", "Batman")
      .formParam("lastName", "I'm Batman")
      .formParam("pass1", "Batman I'm Batman")
      .formParam("numPassengers", "1")
      .formParam("seatType", "Coach")
      .formParam("seatPref", "None")
      .formParam("advanceDiscount", "0")
      .formParam("JSFormSubmit", "off")
      .formParam("buyFlights.x", "20")
      .formParam("buyFlights.y", "3")
      .formParam("cgifields", "saveCC")
  )

  def getLogout = exec(
    http("Logout")
      .get("cgi-bin/welcome.pl?signOff=1")
      .check(status.is(200))
  )

}
