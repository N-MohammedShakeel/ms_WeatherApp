package com.example.ms_weatherapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ms_weatherapp.R
import com.example.ms_weatherapp.WeatherList
import java.text.SimpleDateFormat
import java.util.Locale

class ForeCastAdapter : RecyclerView.Adapter<ForecastHolder>() {

    private var listofforecast = listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.upcomingforecastlist, parent, false)
        return ForecastHolder(view)
    }

    override fun getItemCount(): Int {
        return listofforecast.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        val forecastObject = listofforecast[position]

        // Check if weather list is not null and has at least one item
        val weather = forecastObject.weather.firstOrNull() ?: return

        holder.description.text = weather.description ?: "No description"
        holder.humiditiy.text = forecastObject.main?.humidity?.toString() ?: "N/A"
        holder.windspeed.text = forecastObject.wind?.speed?.toString() ?: "N/A"

        val temperatureFahrenheit = forecastObject.main?.temp
        val temperatureCelsius = temperatureFahrenheit?.minus(273.15)
        val temperatureFormatted = String.format("%.2f", temperatureCelsius ?: 0.0)
        holder.temp.text = "$temperatureFormatted °C"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(forecastObject.dtTxt ?: return)
        val outputFormat = SimpleDateFormat("d MM EEEE", Locale.getDefault())
        val dateanddayname = outputFormat.format(date)
        holder.dateDayName.text = dateanddayname

        // Update weather icons based on the weather condition
        when (weather.icon) {
            "01d", "01n" -> {
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.c_s)
            }
            "02d", "02n" -> {
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            "03d", "03n" -> {
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.scattered_clouds)
            }
            "10d", "10n" -> {
                holder.imageGraphic.setImageResource(R.drawable.chance_rain)
                holder.smallIcon.setImageResource(R.drawable.chance_rain)
            }
            "04d", "04n" -> {
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.scattered_clouds)
            }
            "09d", "09n" -> {
                holder.imageGraphic.setImageResource(R.drawable.chance_rain)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            "11d", "11n" -> {
                holder.imageGraphic.setImageResource(R.drawable.thunder)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            "13d", "13n" -> {
                holder.imageGraphic.setImageResource(R.drawable.snow)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            "50d", "50n" -> {
                holder.imageGraphic.setImageResource(R.drawable.mist)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
        }
    }

    fun setList(newList: List<WeatherList>) {
        this.listofforecast = newList
        notifyDataSetChanged() // Notify adapter that data has changed
    }
}

class ForecastHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageGraphic: ImageView = itemView.findViewById(R.id.imageGraphic)
    val description: TextView = itemView.findViewById(R.id.weatherDescr)
    val humiditiy: TextView = itemView.findViewById(R.id.humidity)
    val windspeed: TextView = itemView.findViewById(R.id.windSpeed)
    val temp: TextView = itemView.findViewById(R.id.tempDisplayForecast)
    val smallIcon: ImageView = itemView.findViewById(R.id.smallicon)
    val dateDayName: TextView = itemView.findViewById(R.id.dayDateText)
}
