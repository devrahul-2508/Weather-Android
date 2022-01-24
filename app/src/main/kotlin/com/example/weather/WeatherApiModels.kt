package com.example.weather

data class WeatherCondition(
  val id: String,
  val main: String,
  val description: String,
)

data class CurrentWeatherResponse(
  val weather: List<WeatherCondition>,
  val main: WeatherMain,
  val wind: WeatherWind,
  val name: String,
) {
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

data class HourlyForecastResponse(
  val current: HourlyForecast,
  val hourly: List<HourlyForecast>
) {

  data class HourlyForecast(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Double,
    val humidity: Int,
    val weather: List<WeatherCondition>,
  )
}
