package com.example.ms_weatherapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ms_weatherapp.R
import com.example.ms_weatherapp.WeatherList
import java.text.SimpleDateFormat
import java.util.Locale

class ForeCastAdapter: RecyclerView.Adapter<ForecastHolder>(){

    private var listofforecast = listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {

        val view  = LayoutInflater.from(parent.context).inflate(R.layout.upcomingforecastlist,parent,false)
        return ForecastHolder(view)

    }

    override fun getItemCount(): Int {

        return listofforecast.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {

        val forecastObject = listofforecast[position]

        for (i in forecastObject.weather){
            holder.description.text = i.description!!

        }

        holder.humiditiy.text = forecastObject.main!!.humidity.toString()
        holder.windspeed.text = forecastObject.wind!!.speed.toString()


        val temperatureFahrenheit = forecastObject.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f",temperatureCelsius)

        // for ° type alt+0176
        holder.temp.text = "$temperatureFormatted °C"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(forecastObject.dtTxt!!)
        val outputFormat = SimpleDateFormat("d MM EEEE",Locale.getDefault())
        val dateanddayname = outputFormat.format(date!!)

        holder.dateDayName.text = dateanddayname

        for (i in forecastObject.weather){

            if (i.icon == "01d"){
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.c_s)
            }
            if (i.icon == "01n"){
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.c_s)
            }
            if (i.icon == "02d"){
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            if (i.icon == "02n"){
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            if (i.icon == "03d" || i.icon == "03n"){
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.scattered_clouds)
            }
            if (i.icon == "10d"){
                holder.imageGraphic.setImageResource(R.drawable.chance_rain)
                holder.smallIcon.setImageResource(R.drawable.chance_rain)
            }
            if (i.icon == "10n"){
                holder.imageGraphic.setImageResource(R.drawable.chance_rain)
                holder.smallIcon.setImageResource(R.drawable.chance_rain)
            }
            if (i.icon == "04d" || i.icon == "04n"){
                holder.imageGraphic.setImageResource(R.drawable.sun)
                holder.smallIcon.setImageResource(R.drawable.scattered_clouds)
            }

            if (i.icon == "09d" || i.icon == "09n"){
                holder.imageGraphic.setImageResource(R.drawable.chance_rain)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            if (i.icon == "11d" || i.icon == "11n"){
                holder.imageGraphic.setImageResource(R.drawable.thunder)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            if (i.icon == "13d" || i.icon == "13n"){
                holder.imageGraphic.setImageResource(R.drawable.snow)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
            if (i.icon == "50d" || i.icon == "50n"){
                holder.imageGraphic.setImageResource(R.drawable.mist)
                holder.smallIcon.setImageResource(R.drawable.cloud)
            }
        }
    }

    fun setList(newList : List<WeatherList>){
        this.listofforecast = newList
    }
}

class ForecastHolder(itemView: View): ViewHolder(itemView) {

    val imageGraphic: ImageView = itemView.findViewById(R.id.imageGraphic)
    val description: TextView = itemView.findViewById(R.id.weatherDescr)
    val humiditiy: TextView = itemView.findViewById(R.id.humidity)
    val windspeed: TextView = itemView.findViewById(R.id.windSpeed)
    val temp: TextView = itemView.findViewById(R.id.tempDisplayForecast)
    val smallIcon: ImageView = itemView.findViewById(R.id.smallicon)
    val dateDayName: TextView = itemView.findViewById(R.id.dayDateText)
}
