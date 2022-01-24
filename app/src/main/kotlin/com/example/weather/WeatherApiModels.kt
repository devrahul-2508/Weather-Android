package com.example.weather

data class WeatherResponse(
  val weather: List<WeatherCondition>,
  val main: WeatherMain,
  val wind: WeatherWind,
  val name: String,
) {
  data class WeatherCondition(
    val id: String,
    val main: String,
    val description: String,
  )

  data class WeatherMain(
    val temp: Double,
    val feels_like: Double,
    val pressure: Double,
    val humidity: Int
  )

  data class WeatherWind(
    val speed: Double
  )
}
