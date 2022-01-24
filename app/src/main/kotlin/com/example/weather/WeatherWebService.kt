package com.example.weather

import com.example.weather.BuildConfig.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherWebService {
  @GET("weather")
  suspend fun getCurrentWeatherForLocation(
    @Query("lat") latitude: String,
    @Query("lon") longitude: String,
    @Query("apiKey") apiKey: String = API_KEY,
    @Query("units") units: String = "metric",
  ): CurrentWeatherResponse

  @GET("onecall")
  suspend fun getHourlyForecastForLocation(
    @Query("lat") latitude: String,
    @Query("lon") longitude: String,
    @Query("apiKey") apiKey: String = API_KEY,
    @Query("units") units: String = "metric",
  ): HourlyForecastResponse

}
