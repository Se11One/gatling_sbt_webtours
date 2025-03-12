package com.example.webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.example.webtours.CommonScenario._
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

class SearchForMaximum extends Simulation {

  val config = ConfigFactory.load()
  val baseUrl = config.getString("webtours.baseUrl")

  val httpConf = http.baseUrl(baseUrl)

  // Определяем параметры теста
  val maxUsers = 50 // Максимальное количество пользователей
  val stepUsers = 5 // Количество пользователей на ступень (10% от максимума)
  val stagesNumber = maxUsers / stepUsers // Количество ступеней
  val stageDuration = 5.minutes // Длительность каждой ступени
  val rampDuration = 1.minute // Время разгона между ступенями

  setUp(
    scn.inject(
      incrementUsersPerSec(stepUsers) // Увеличение пользователей на каждом этапе
        .times(stagesNumber) // Количество ступеней
        .eachLevelLasting(stageDuration) // Длительность каждой ступени
        .separatedByRampsLasting(rampDuration) // Время разгона между ступенями
        .startingFrom(0) // Начало нагрузки с 0 пользователей
    ).protocols(httpConf)
  )
}
