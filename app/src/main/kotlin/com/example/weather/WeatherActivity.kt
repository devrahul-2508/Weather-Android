package com.example.weather

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.weather.BuildConfig.API_BASE_URL
import com.example.weather.WeatherResponse.WeatherCondition
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class WeatherActivity : AppCompatActivity(), CoroutineScope {
  private val locationText: TextView by lazy { findViewById(R.id.weather_locationTitle) }
  private val progressBar: View by lazy { findViewById(R.id.weather_progressBar) }
  private val conditionView: View by lazy { findViewById(R.id.weather_condition) }
  private val conditionImageView: ImageView by lazy { findViewById(R.id.weather_conditionIcon) }
  private val conditionTitleView: TextView by lazy { findViewById(R.id.weather_conditionTitle) }
  private val conditionDateView: TextView by lazy { findViewById(R.id.weather_conditionDate) }
  private val conditionTemperatureView: TextView by lazy { findViewById(R.id.weather_conditionTemperature) }
  private val windSpeedTextView: TextView by lazy { findViewById(R.id.weather_windSpeed) }
  private val feelsLikeTextView: TextView by lazy { findViewById(R.id.weather_feelsLike) }
  private val humidityTextView: TextView by lazy { findViewById(R.id.weather_humidity) }
  private val pressureTextView: TextView by lazy { findViewById(R.id.weather_pressure) }

  private val httpClient: OkHttpClient = OkHttpClient.Builder().build()

  private val moshiConverter: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val retrofit: Retrofit = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(API_BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshiConverter))
    .build()

  private val webService: WeatherWebService = retrofit.create()

  override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_weather)
  }

  override fun onStart() {
    super.onStart()

    launch {
      withContext(Dispatchers.Main) {
        locationText.isVisible = false
        conditionView.isVisible = false
        progressBar.isVisible = true
      }

      // TODO: Get user's location
      // TODO: Handle network failure
      val response: WeatherResponse = webService.getWeatherForLocation("-37.8197304", "144.9516833")

      val condition: WeatherCondition = response.weather.first()

      @DrawableRes val conditionDrawable: Int = when {
        condition.id.startsWith("2") -> R.drawable.ic_weather_2xx
        condition.id.startsWith("3") -> R.drawable.ic_weather_3xx
        condition.id.startsWith("5") -> R.drawable.ic_weather_5xx
        condition.id.startsWith("6") -> R.drawable.ic_weather_6xx
        condition.id.startsWith("7") -> R.drawable.ic_weather_7xx
        condition.id.startsWith("8") -> R.drawable.ic_weather_8xx
        else -> R.drawable.ic_weather_801
      }

      val locationName: String = response.name
      val conditionTitle: String = condition.main
      // TODO: Use the correct date format
      val conditionDate: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
      val conditionTemp: String = "${response.main.temp.toInt()}°"
      val windSpeedText = "${response.wind.speed} m/s"
      val feelsLikeText = "${response.main.feels_like} °"
      val humidityText = "${response.main.humidity} %"
      val pressureText = "${response.main.pressure} hPa"

      withContext(Dispatchers.Main) {
        locationText.isVisible = true
        conditionView.isVisible = true
        progressBar.isVisible = false
        locationText.text = locationName
        conditionImageView.setImageResource(conditionDrawable)
        conditionTitleView.text = conditionTitle
        conditionDateView.text = conditionDate
        conditionTemperatureView.text = conditionTemp
        windSpeedTextView.text = windSpeedText
        feelsLikeTextView.text = feelsLikeText
        humidityTextView.text = humidityText
        pressureTextView.text = humidityText
      }
    }
  }
}
