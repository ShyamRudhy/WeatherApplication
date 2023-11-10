package com.shyam.weatherapp.ui.weather_home
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shyam.weatherapp.base.PrivateSharedPrefManager
import com.shyam.weatherapp.data.repository.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.shyam.weatherapp.data.Result
import com.shyam.weatherapp.data.api.model.GetWeatherResponse


@HiltViewModel
class WeatherHomePageViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    @Inject
    lateinit var privateSharedPreferencesManager: PrivateSharedPrefManager

     init {

    }

    private val _weatherData = MutableLiveData<GetWeatherResponse>()
     val weatherData:LiveData<GetWeatherResponse> =_weatherData


    private val _weatherResponse = MutableLiveData<Result<GetWeatherResponse>>()
    val weatherResponse:LiveData<Result<GetWeatherResponse>> =_weatherResponse


    suspend fun getWeatherServiceCall(cityName: String){
        _weatherResponse.postValue(Result.loading(null))
        val res = homeRepository.getWeatherDataFromService(cityName)
        _weatherResponse.postValue(res)

    }

    fun setWeatherData(data :GetWeatherResponse){
        _weatherData.value = data
    }






}