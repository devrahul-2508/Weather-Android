package com.example.weather

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weather.HourlyForecastResponse.HourlyForecast
import com.example.weather.WeatherActivity.Companion.getIcon
import com.example.weather.WeatherArrayAdapter.WeatherAdapterViewHolder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

class WeatherArrayAdapter(private val forecasts: List<HourlyForecast>) :
  RecyclerView.Adapter<WeatherAdapterViewHolder>() {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapterViewHolder =
    WeatherAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_forcast_item, parent, false))

  override fun onBindViewHolder(holder: WeatherAdapterViewHolder, position: Int) {
    val forecast: HourlyForecast = forecasts[position]
    holder.display(forecast)
  }

  override fun getItemCount(): Int = forecasts.size

  inner class WeatherAdapterViewHolder(val view: View) : ViewHolder(view) {
    private val timeTextView: TextView = view.findViewById(R.id.weather_item_time)
    private val iconView: ImageView = view.findViewById(R.id.weather_item_icon)
    private val tempTextView: TextView = view.findViewById(R.id.weather_item_temp)

    fun display(forecast: HourlyForecast) {
      timeTextView.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(forecast.dt), UTC))
      val icon: Drawable = getIcon(view.context, forecast.weather.first().id)
      iconView.setImageDrawable(icon)
      tempTextView.text = "${forecast.temp}°"
    }
  }
}
