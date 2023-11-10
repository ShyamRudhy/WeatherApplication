package com.shyam.weatherapp.data.repository.home
import com.shyam.weatherapp.common.Constants
import com.shyam.weatherapp.data.api.ApiService
import com.shyam.weatherapp.data.api.BaseRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton
import com.shyam.weatherapp.data.Result
import com.shyam.weatherapp.data.api.model.GetWeatherResponse


@Singleton
class HomeRepository @Inject constructor(private val service: ApiService) : BaseRemoteDataSource() {

     suspend fun getWeatherDataFromService(cityName: String): Result<GetWeatherResponse> {
        return getResult { service.getWeatherData(cityName, Constants.API_KEY,"metric") }
    }



}