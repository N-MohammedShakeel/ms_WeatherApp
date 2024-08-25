package com.example.ms_weatherapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.ms_weatherapp.adapter.ForeCastAdapter
import com.example.ms_weatherapp.databinding.ActivityForeCastBinding
import com.example.ms_weatherapp.mvvm.WeatherVm

class ForeCastActivity : AppCompatActivity() {

    private lateinit var adapterForeCastAdapter: ForeCastAdapter
    private lateinit var viM: WeatherVm
    private lateinit var rvF: RecyclerView
    private lateinit var binding: ActivityForeCastBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForeCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viM = ViewModelProvider(this).get(WeatherVm::class.java)
        adapterForeCastAdapter = ForeCastAdapter()

        rvF = binding.rvForeCast // Assigning the value to rvForeCast

        val sharedPrefs = SharedPrefs.getInstance(applicationContext)
        val city = sharedPrefs.getValueOrNull("city")

        if (city != null) {
            viM.getForecastupcoming(city)
        } else {
            viM.getForecastupcoming()
        }

        viM.forecastWeatherLiveData.observe(this, Observer { forecastList ->
            val setNewList = forecastList as List<WeatherList>

            Log.d("Forecast LiveData", setNewList.toString())
            adapterForeCastAdapter.setList(setNewList)
            rvF.adapter = adapterForeCastAdapter

            val isAdapterAttached = rvF.adapter != null
            Log.d("forecast", "Adapter is attached: $isAdapterAttached")
        })
    }
}
