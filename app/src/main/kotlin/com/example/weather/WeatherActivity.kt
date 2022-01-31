package com.example.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.BuildConfig.API_BASE_URL
import com.example.weather.HourlyForecastResponse.HourlyForecast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger
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
  private val humidityLayout: LinearLayout by lazy { findViewById(R.id.weather_humidityLayout) }
  private val humidityTextView: TextView by lazy { findViewById(R.id.weather_humidity) }
  private val pressureLayout: LinearLayout by lazy { findViewById(R.id.weather_pressureLayout) }
  private val pressureTextView: TextView by lazy { findViewById(R.id.weather_pressure) }
  private val forecastRecyclerView: RecyclerView by lazy { findViewById(R.id.weather_forecast_list) }

  private val httpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor(logger = Logger.DEFAULT))
    .build()

  private val moshiConverter: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val retrofit: Retrofit = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(API_BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshiConverter))
    .build()

  private val webService: WeatherWebService = retrofit.create()

  private val dateTimeFormatter = DateTimeFormatter.ofPattern(" E',' dd MMMM")

  override val coroutineContext: CoroutineContext = Dispatchers.Main

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_weather)
  }

  override fun onStart() {
    super.onStart()

    locationText.isVisible = false
    conditionView.isVisible = false
    progressBar.isVisible = true

    launch {
      // TODO: Handle network failure
      val currentWeatherResponse: CurrentWeatherResponse =
        // TODO: Get user's location
        webService.getCurrentWeatherForLocation("-37.8197304", "144.9516833")

      val condition: WeatherCondition = currentWeatherResponse.weather.first()
      val locationName: String = currentWeatherResponse.name
      val conditionIcon: Drawable = getIcon(this@WeatherActivity, condition.id)
      val conditionTitle: String = condition.main
      val conditionDate: String = LocalDateTime.now().format(dateTimeFormatter)
      val conditionTemp: String = "${currentWeatherResponse.main.temp.toInt()}°"
      val windSpeedText = "${currentWeatherResponse.wind.speed} m/s"
      val feelsLikeText = "${currentWeatherResponse.main.feels_like} °"
      val humidityText = "${currentWeatherResponse.main.humidity} %"
      val pressureText = "${currentWeatherResponse.main.pressure} hPa"

      locationText.isVisible = true
      conditionView.isVisible = true
      progressBar.isVisible = false
      locationText.text = locationName
      conditionImageView.setImageDrawable(conditionIcon)
      conditionTitleView.text = conditionTitle
      conditionDateView.text = conditionDate
      conditionTemperatureView.text = conditionTemp
      windSpeedTextView.text = windSpeedText
      feelsLikeTextView.text = feelsLikeText
      humidityLayout.isVisible = !BuildConfig.IS_LITE
      pressureLayout.isVisible = !BuildConfig.IS_LITE
      humidityTextView.text = humidityText
      pressureTextView.text = pressureText

      // TODO: Handle network failure
      val forecastResponse: HourlyForecastResponse =
        // TODO: Get user's location
        webService.getHourlyForecastForLocation("-37.8197304", "144.9516833")

      val forecasts: MutableList<HourlyForecast> = mutableListOf()

      for (forecast in forecastResponse.hourly) {
        if (BuildConfig.IS_LITE && forecasts.size > 5) break
        forecasts.add(forecast)
      }

      val adapter = WeatherArrayAdapter(forecasts)
      val layoutManager = LinearLayoutManager(applicationContext)
      layoutManager.orientation = LinearLayoutManager.HORIZONTAL
      forecastRecyclerView.layoutManager = layoutManager
      forecastRecyclerView.adapter = adapter
    }
  }

  companion object {
    private var iconCache: MutableMap<String, Drawable?> = mutableMapOf()

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getIcon(context: Context, id: String): Drawable {
      val cachedIcon: Drawable? = iconCache[id]
      if (cachedIcon != null) return cachedIcon

      val conditionDrawable: Drawable? = when {
        id.startsWith("2") -> context.getDrawable(R.drawable.ic_weather_2xx)
        id.startsWith("3") -> context.getDrawable(R.drawable.ic_weather_3xx)
        id.startsWith("5") -> context.getDrawable(R.drawable.ic_weather_5xx)
        id.startsWith("6") -> context.getDrawable(R.drawable.ic_weather_6xx)
        id.startsWith("7") -> context.getDrawable(R.drawable.ic_weather_7xx)
        id.startsWith("8") -> context.getDrawable(R.drawable.ic_weather_8xx)
        else -> context.getDrawable(R.drawable.ic_weather_801)
      }

      iconCache[id] = conditionDrawable
      return requireNotNull(conditionDrawable)
    }
  }
}
