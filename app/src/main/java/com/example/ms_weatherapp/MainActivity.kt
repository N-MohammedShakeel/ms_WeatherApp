package com.example.ms_weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ms_weatherapp.adapter.WeatherToday
import com.example.ms_weatherapp.databinding.ActivityMainBinding
import com.example.ms_weatherapp.mvvm.WeatherVm
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var vm : WeatherVm
    private lateinit var adapter: WeatherToday


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProvider(this).get(WeatherVm::class.java)

        adapter = WeatherToday()


        binding.lifecycleOwner = this
        binding.vm = vm

        vm.getWeather()



        // whenever app rons clear the city thut se nod searched previously
        val sharedPrefs = SharedPrefs.getInstance(context = applicationContext)
        sharedPrefs.clearCityValue()

        vm.todayWeatherLiveData.observe(this, Observer {

            val setNewlist = it as List<WeatherList>

            adapter.setList(setNewlist)
            binding.forecastRecyclerView.adapter = adapter

            val isAdapterAttached = binding.forecastRecyclerView.adapter != null
            Log.d("weather Today", " weather today ,Adapter is attached: $isAdapterAttached")


        })
        vm.closetorexactlysameweatherdata.observe(this, Observer {

            val temperatureFahrenheit = it!!.main?.temp
            val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
            val temperatureFormatted = String.format("%.2f", temperatureCelsius)

            for (i in it.weather) {
                binding.descMain.text = i.description
            }

            binding.tempMain.text = "$temperatureFormatted °C"

            binding.humidityValue.text = it.main!!.humidity.toString()
            binding.windspeedValue.text = it.wind!!.speed.toString()

            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(it.dtTxt!!)
            val outputFormat = SimpleDateFormat("d MM EEEE", Locale.getDefault())
            val dateanddayname = outputFormat.format(date!!)

            binding.dataDayMain.text = dateanddayname

            binding.chanceofrain.text = "${it.pop.toString()}%"

            //setting the icon
            for (i in it.weather) {

                if (i.icon == "01d") {
                    binding.imageMain.setImageResource(R.drawable.c_s)
                }
                if (i.icon == "01n") {
                    binding.imageMain.setImageResource(R.drawable.c_s)
                }
                if (i.icon == "02d") {
                    binding.imageMain.setImageResource(R.drawable.cloud)
                }
                if (i.icon == "02n") {
                    binding.imageMain.setImageResource(R.drawable.cloud)
                }
                if (i.icon == "03d" || i.icon == "03n") {
                    binding.imageMain.setImageResource(R.drawable.scattered_clouds)
                }
                if (i.icon == "10d") {
                    binding.imageMain.setImageResource(R.drawable.chance_rain)
                }
                if (i.icon == "10n") {
                    binding.imageMain.setImageResource(R.drawable.chance_rain)
                }
                if (i.icon == "04d" || i.icon == "04n") {
                    binding.imageMain.setImageResource(R.drawable.scattered_clouds)
                }

                if (i.icon == "09d" || i.icon == "09n") {
                    binding.imageMain.setImageResource(R.drawable.chance_rain)
                }
                if (i.icon == "11d" || i.icon == "11n") {
                    binding.imageMain.setImageResource(R.drawable.thunder)
                }
                if (i.icon == "13d" || i.icon == "13n") {
                    binding.imageMain.setImageResource(R.drawable.snow)
                }
                if (i.icon == "50d" || i.icon == "50n") {
                    binding.imageMain.setImageResource(R.drawable.mist)
                }
            }

        })

        // Check for location permissions
        if (checkLocationPermissions()) {
            //Permissions are granted, proceed to get the current location
            getCurrentLocation()
        } else {
            //Request location permissions
            requestLocationPermissions()
        }

        binding.next5Days.setOnClickListener {

            startActivity(Intent(this, ForeCastActivity::class.java))
        }


        val searchEditText = binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.WHITE)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {



            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextSubmit(query: String?): Boolean {
                val Prefs = SharedPrefs.getInstance(context = applicationContext)
                Prefs.setValue("city",query!!)

                if (!query.isNullOrEmpty()){
                    vm.getWeather(query)

                    binding.searchView.setQuery("",false)
                    binding.searchView.clearFocus()
                    binding.searchView.isIconified = true
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true

            }
        })



    }



    private fun checkLocationPermissions(): Boolean {

        val fineLocationPermission= ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)

        val coarseLocationPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)

        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED


    }

    private fun requestLocationPermissions() {

        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),

        Utils.PERMISSION_REQUEST_CODE
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode == Utils. PERMISSION_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                getCurrentLocation()
            } else {
                //permission denied


            }
        }

    }

    private fun getCurrentLocation() {

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                // Use the latitude and Longitude values as needed
                //........

                val myprefs = SharedPrefs(this)
                myprefs.setValue("lon",longitude.toString())
                myprefs.setValue("lat", latitude.toString())

                // Example:Display latitude and longitude in logs

                Toast.makeText(this,"Latitude$latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()
                Log.d("Current Location","Latitude: $latitude, Longitude: $longitude")

                // Reverse geocode the location to get address information
                //reverseGeocodeLocation(latitude, longitude)

            } else{

            // Location is null, handle accordingly
            // For example, request location updates or show an error message

        }

    } else {

    // Location permission not granted, handle accordingly
    // For example, show an error message or disable location-based features


    }

    }

}


