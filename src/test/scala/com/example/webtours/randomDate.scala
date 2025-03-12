package com.example.webtours

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

object RandomDate {

  // Функция для генерации случайной даты
  def generateRandomDate(start: LocalDate, end: LocalDate): LocalDate = {
    val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end).toInt
    start.plusDays(Random.nextInt(daysBetween + 1))
  }

  // Генерируем случайные даты
  def generateDates(): (String, String) = {
    val now = LocalDate.now() // Текущая дата
    val startDepartDate = now // Начальная дата - сегодня
    val endDepartDate = startDepartDate.plusDays(30) // Конечная дата - 30 дней вперёд

    // Генерируем случайную дату отправления
    val departDate = generateRandomDate(startDepartDate, endDepartDate)
      .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))

    // Генерируем случайную дату возвращения, начиная с даты отправления
    val returnDate = generateRandomDate(
      LocalDate.parse(departDate, DateTimeFormatter.ofPattern("MM/dd/yyyy")),
      endDepartDate
    ).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))

    (departDate, returnDate)
  }
}
