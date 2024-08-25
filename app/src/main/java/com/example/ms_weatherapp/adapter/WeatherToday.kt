package com.example.ms_weatherapp.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import java.util.Calendar
import java.util.Locale

class WeatherToday : RecyclerView.Adapter<TodayHolder>() {

    private var listOfTodayWeather = listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todayhourlylist, parent, false)
        return TodayHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfTodayWeather.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
        val todayForeCast = listOfTodayWeather[position]

        holder.timeDisplay.text = todayForeCast.dtTxt?.substring(11, 16) ?: "N/A"

        val temperatureFahrenheit = todayForeCast.main?.temp
        val temperatureCelsius = temperatureFahrenheit?.minus(273.15)
        val temperatureFormatted = String.format("%.2f", temperatureCelsius ?: 0.0)
        holder.tempDisplay.text = "$temperatureFormatted °C"

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = dateFormat.format(calendar.time)

        val timeofapi = todayForeCast.dtTxt?.split(" ")?.getOrNull(1) ?: "N/A"

        Log.e("time", "formatted time: $formattedTime, timeofapi: $timeofapi")

        for (i in todayForeCast.weather) {
            when (i.icon) {
                "01d", "01n" -> holder.imageDisplay.setImageResource(R.drawable.c_s)
                "02d", "02n" -> holder.imageDisplay.setImageResource(R.drawable.scattered_clouds)
                "03d", "03n" -> holder.imageDisplay.setImageResource(R.drawable.scattered_clouds)
                "10d", "10n" -> holder.imageDisplay.setImageResource(R.drawable.chance_rain)
                "04d", "04n" -> holder.imageDisplay.setImageResource(R.drawable.cloud)
                "09d", "09n" -> holder.imageDisplay.setImageResource(R.drawable.chance_rain)
                "11d", "11n" -> holder.imageDisplay.setImageResource(R.drawable.thunder)
                "13d", "13n" -> holder.imageDisplay.setImageResource(R.drawable.snow)
                "50d", "50n" -> holder.imageDisplay.setImageResource(R.drawable.mist)
            }
        }
    }

    fun setList(listOfToday: List<WeatherList>) {
        this.listOfTodayWeather = listOfToday
        notifyDataSetChanged() // Notify the adapter about data changes
    }
}

class TodayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageDisplay: ImageView = itemView.findViewById(R.id.imageDisplay)
    val tempDisplay: TextView = itemView.findViewById(R.id.tempDisplay)
    val timeDisplay: TextView = itemView.findViewById(R.id.timeDisplay)
}
