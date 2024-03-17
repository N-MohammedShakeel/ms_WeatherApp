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

class WeatherToday: RecyclerView.Adapter<TodayHolder>() {

     private var listOfTodayWeather =  listOf<WeatherList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.todayhourlylist,parent,false)
        return TodayHolder(view)

    }

    override fun getItemCount(): Int {

        return listOfTodayWeather.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TodayHolder, position: Int) {

        val todayForeCast = listOfTodayWeather[position]

        holder.timeDisplay.text = todayForeCast.dtTxt!!.substring(11,16).toString()

        val temperatureFahrenheit = todayForeCast.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f",temperatureCelsius)

        // for ° type alt+0176
        holder.tempDisplay.text = "$temperatureFormatted °C"

        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH::mm")
        val formattedTime = dateFormat.format(calender.time)

        val timeofapi = todayForeCast.dtTxt!!.split(" ")
        val partafterspace = timeofapi[1]

        Log.e("time" , "formatted time: ${formattedTime}, timeofapi: ${partafterspace}")

        for(i in todayForeCast.weather){
            if(i.icon == "O1d"){
                holder.imageDisplay.setImageResource(R.drawable.c_s)

            }
            if(i.icon == "O1n"){
                holder.imageDisplay.setImageResource(R.drawable.c_s)

            }
            if(i.icon == "O2d"){
                holder.imageDisplay.setImageResource(R.drawable.scattered_clouds)

            }
            if(i.icon == "O2n"){
                holder.imageDisplay.setImageResource(R.drawable.scattered_clouds)

            }
            if(i.icon == "03d" || i.icon == "03n"){
                holder.imageDisplay.setImageResource(R.drawable.scattered_clouds)

            }
            if(i.icon == "10d"){
                holder.imageDisplay.setImageResource(R.drawable.chance_rain)

            }
            if(i.icon == "10n"){
                holder.imageDisplay.setImageResource(R.drawable.chance_rain)

            }
            if(i.icon == "O4d" || i.icon == "04n"){
                holder.imageDisplay.setImageResource(R.drawable.cloud)

            }
            if(i.icon == "O9d" || i.icon == "09n"){
                holder.imageDisplay.setImageResource(R.drawable.chance_rain)

            }


            if(i.icon == "11d" || i.icon == "11n"){
                holder.imageDisplay.setImageResource(R.drawable.thunder)

            }

            if(i.icon == "13d" || i.icon == "13n"){
                holder.imageDisplay.setImageResource(R.drawable.snow)

            }

            if(i.icon == "50d" || i.icon == "50n"){
                holder.imageDisplay.setImageResource(R.drawable.mist)

            }

        }


    }

    fun setList(listOfToday : List<WeatherList>){
        this.listOfTodayWeather = listOfToday
    }

}

class TodayHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val imageDisplay : ImageView = itemView.findViewById(R.id.imageDisplay)
    val tempDisplay : TextView = itemView.findViewById(R.id.tempDisplay)
    val timeDisplay : TextView = itemView.findViewById(R.id.timeDisplay)

}