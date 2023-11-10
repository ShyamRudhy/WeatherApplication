package com.shyam.weatherapp.ui.weather_home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.shyam.weatherapp.R
import com.shyam.weatherapp.base.BaseFragment
import com.shyam.weatherapp.common.Utils
import com.shyam.weatherapp.common.service.ConnectionLiveData
import com.shyam.weatherapp.data.Result
import com.shyam.weatherapp.data.api.model.GetWeatherResponse
import com.shyam.weatherapp.databinding.FragmentWeatherHomePageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


@AndroidEntryPoint
class WeatherHomePageFragment : BaseFragment<FragmentWeatherHomePageBinding>() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var connectionLiveData: ConnectionLiveData

    private lateinit  var _binding: FragmentWeatherHomePageBinding
    private val binding get() = _binding
    private val sharedViewModel: WeatherHomePageViewModel by activityViewModels() //shared viewModel

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                showMessage("Permission Granted")
                initialiseGeoLocation()
        } else {
                Log.i("Permission: ", "Denied")
                showMessage("Permission Denied")

            }
        }

    private  lateinit var cityName : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWeatherHomePageBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
        }
        checkNetwork()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickRequestPermission(view)
        initialiseObservers()
        searchCity()
        refreshSwipe()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkNetwork(){
        /*Check network connection*/
        connectionLiveData = ConnectionLiveData(requireContext())
        connectionLiveData.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let {
                if (!it) {
                    showSnackBarErrorMsg("No Internet Connection")
                } else {
                    showSnackBarMsg("Internet Connected")
                }}
        }
    }

    private fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                showMessage("Permission Granted")
                initialiseGeoLocation()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                showMessage("Location is mandatory")
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun initialiseGeoLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()

    }

    @SuppressLint("SetTextI18n")
    private fun initialiseUI(data: GetWeatherResponse){
        val humidity = data.main.humidity
        val windSpeed = data.wind.speed
        val sunRise = data.sys.sunrise.toLong()
        val sunSet = data.sys.sunset.toLong()
        val sunLevel = data.main.pressure
        val condition = data.weather.firstOrNull()?.main?:"unKnown"
        val maxTemp = data.main.temp_max
        val minTemp = data.main.temp_min

        binding.weather.text = condition
        binding.condition.text = condition
        binding.maxTemp.text = "Max Temp : $maxTemp °C"
        binding.minTemp.text = "Min Temp : $minTemp °C"
        binding.humidity.text = "$humidity %"
        binding.windspeed.text = "$windSpeed m/s"
        binding.sunrise.text = Utils.time(sunRise)
        binding.sunset.text = Utils.time(sunSet)
        binding.sea.text = "$sunLevel hpa"
        binding.date.text =Utils.dayName(System.currentTimeMillis())
        binding.day.text =Utils.date()
        binding.cityName.text = data.name

        /**/
        changeBgToWeatherCondition(condition)

        binding.executePendingBindings()

    }

    private fun changeBgToWeatherCondition(conditions: String) {
        when (conditions) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Party Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain", "Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" , "Snow" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // Got last known location. In some situations, this can be null.
                if (location != null) {
                    // Get the city name from the location
                    cityName = getCityName(requireActivity(), location.latitude, location.longitude)
                    /*service call*/
                    getWeatherData(cityName)
                    binding.executePendingBindings()
                    // Do something with the city name
                }
            }
                .addOnFailureListener { e ->
                    // Handle failure
                    showMessage("Failed to get location: ${e.message}")
                }
        } else
            showMessage("Location permission not granted")

    }


    private fun getCityName(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                // Extract the city name from the address
                val city = addresses[0].locality
                return city ?: "Unknown"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "Unknown"
    }

    private fun refreshSwipe() {
        binding.refreshScreen.setOnRefreshListener {
            getWeatherData(cityName)
            binding.refreshScreen.isRefreshing =false
        }
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getWeatherData(query)
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }
    

    private fun getWeatherData(cityName: String){
        /** Service call */
        lifecycleScope.launch {
            sharedViewModel.getWeatherServiceCall(cityName)
        }
    }
    private fun initialiseObservers(){
        sharedViewModel.weatherResponse.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    showLoadingDialog(false)
                    result.data?.let {
                        sharedViewModel.setWeatherData(result.data)
                        initialiseUI(result.data)
                        binding.executePendingBindings()
                        showMessage("Weather Updated...")
                    }
                }
                Result.Status.LOADING -> {
                    showLoadingDialog(true)
                }
                Result.Status.ERROR -> {
                    showLoadingDialog(false)
                    showMessage(result.message.toString())

                }
            }
        }
    }


}