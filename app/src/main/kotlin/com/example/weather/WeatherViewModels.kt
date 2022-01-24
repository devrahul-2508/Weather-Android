package com.example.weather

sealed class WeatherState {
  object InProgress : WeatherState()
  data class Failed(val isNetworkError: Boolean) : WeatherState()
  data class Ready(val response: WeatherResponse) : WeatherState()
}
