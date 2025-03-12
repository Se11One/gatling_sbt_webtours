package com.example.webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import com.example.webtours.CommonScenario._

class StepTest extends Simulation {

  val config = ConfigFactory.load()
  val baseUrl = config.getString("webtours.baseUrl")

  val httpConf = http.baseUrl(baseUrl)

  val initialUsers = 5 // Начальное количество пользователей
  val stepIncrement = 5 // На сколько пользователей увеличиваем на каждом шаге
  val stepDuration = 120.seconds // Длительность одной ступени (2 минуты)
  val steps = 10 // Количество ступеней

  //  setUp(
  //    scn.inject(
  //      (1 to steps).map { step =>
  //        val usersToAdd = initialUsers + (stepIncrement * (step - 1)) // Увеличиваем количество пользователей на каждом шаге
  //        rampUsers(usersToAdd) during (stepDuration) // Плавный рост на каждой ступени
  //      }
  //    )
  //  ).protocols(httpConf)

  setUp(
    scn.inject(
      (1 to steps).map { step =>
        val usersToAdd = step * stepIncrement // На каждой ступени добавляем на 5 больше
        rampUsers(usersToAdd) during (stepDuration)
      }
    ).protocols(httpConf))
}
