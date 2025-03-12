package com.example.webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.example.webtours.CommonScenario._
import com.typesafe.config.ConfigFactory

class LoadTest extends Simulation {

  val config = ConfigFactory.load()
  val baseUrl = config.getString("webtours.baseUrl")

  val httpConf = http.baseUrl(baseUrl)

  setUp(
    scn.inject(
      constantUsersPerSec(2) during (1)
    ).protocols(httpConf),
  )

}