package com.example.webtours

import com.example.webtours.Actions._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object CommonScenario {

  val scn = scenario("1st scenario")
    .exec(getMainPage)
    .exec(getUserSession)
    .exec(login)
    .exec(getFlightsPage)
    .exec(getSities)
    .exec(reservationsFindFlight)
    .exec(reservationsPaymentDetails)
    .exec(invoice)
    .exec(getLogout)
}