package com.example.webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.example.webtours.CommonScenario._
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration.DurationInt

class ReliabilityTest extends Simulation {

  val config = ConfigFactory.load()
  val baseUrl = config.getString("webtours.baseUrl")

  val httpConf = http.baseUrl(baseUrl)

  setUp(
    scn.inject(
      rampUsers(40) during (10.minutes), // Плавный выход на 40 пользователей за 10 минут
      constantUsersPerSec(40) during (50.minutes) // Удержание 40 пользователей в течение 50 минут
    )
  ).protocols(httpConf)

}