package com.example.ms_weatherapp.mvvm


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ms_weatherapp.City
import com.example.ms_weatherapp.MyApplication
import com.example.ms_weatherapp.SharedPrefs
import com.example.ms_weatherapp.WeatherList
import com.example.ms_weatherapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherVm: ViewModel() {

    private val _forecastWeatherLiveData = MutableLiveData<List<WeatherList>>()
    val forecastWeatherLiveData: LiveData<List<WeatherList>> = _forecastWeatherLiveData

    private val _cityName = MutableLiveData<String?>()
    val ccityName: LiveData<String?> = _cityName


    val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
    val closetorexactlysameweatherdata = MutableLiveData<WeatherList?>()
    val cityName = MutableLiveData<String?>()
    val context = MyApplication.instance

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeather (city: String? = null) = viewModelScope.launch (Dispatchers. IO) {

        val todayWeatherList = mutableListOf<WeatherList>()
        val currentDateTime = LocalDateTime.now()
        val currentDatePattern = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))


        val sharedPrefs = SharedPrefs(context)

        val lat = sharedPrefs.getValue("lat").toString()
        val lon = sharedPrefs.getValue("lon").toString()

        val call = if (city != null) {
            RetrofitInstance.api.getWeatherByCity(city)
        } else {
            RetrofitInstance.api.getCurrentWeather(lat, lon)
        }

        val response = call.execute()
        if (response.isSuccessful) {
            val weatherList = response.body()?.weatherList
            cityName.postValue(response.body()?.city!!.name)

            val presentDate = currentDatePattern

            weatherList?.forEach { weather ->

                // seperate all the weather objects that have the date of today
                if (weather.dtTxt!!.split("\\s".toRegex()).contains(presentDate)) {
                    todayWeatherList.add(weather)

                }
            }

            // if the api time is closect to the system time display that
            val closetWeather = findClosestWeather(todayWeatherList)
            closetorexactlysameweatherdata.postValue(closetWeather)

            todayWeatherLiveData.postValue(todayWeatherList)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun findClosestWeather(weatherList: List<WeatherList>): WeatherList? {

        val systemTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        var closestWeather: WeatherList? =null
        var minTimeDifference = Int.MAX_VALUE
        for (weather in weatherList) {
            val weatherTime = weather.dtTxt!!.substring(11, 16)
            val timeDifference = Math.abs(timeToMinutes (weatherTime) - timeToMinutes(systemTime))

            if (timeDifference < minTimeDifference) {
                minTimeDifference = timeDifference
                closestWeather = weather
            }
        }

        return closestWeather

    }

    private fun timeToMinutes(time: String): Int {

        val parts = time.split( ":")
        return parts[0].toInt() * 60+ parts[1].toInt()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getForecastupcoming(city: String? = null) = viewModelScope.launch(Dispatchers.IO) {
        fetchData(city) { weatherList ->
            val forecastWeatherList = weatherList.filter { !it.dtTxt?.contains(getCurrentDate())!! }
                .filter { it.dtTxt?.substring(11, 16) == "12:00" }
            _forecastWeatherLiveData.postValue(forecastWeatherList)
        }
    }

    private fun fetchData(city: String?, onSuccess: (List<WeatherList>) -> Unit) {
        val lat = SharedPrefs.getInstance(context).getValue("lat").toString()
        val lon = SharedPrefs.getInstance(context).getValue("lon").toString()

        val call = if (city != null) {
            RetrofitInstance.api.getWeatherByCity(city)
        } else {
            RetrofitInstance.api.getCurrentWeather(lat, lon)
        }

        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val weatherList = response.body()?.weatherList
                _cityName.postValue(response.body()?.city?.name)
                weatherList?.let(onSuccess)
            }
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }



}

