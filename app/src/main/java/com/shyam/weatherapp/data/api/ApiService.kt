package com.shyam.weatherapp.data.api

import android.content.Context
import com.shyam.weatherapp.base.PrivateSharedPrefManager
import com.shyam.weatherapp.common.Constants.Companion.BASE_URL
import com.shyam.weatherapp.data.api.model.GetWeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("weather")
   suspend fun getWeatherData(
        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units") units:String
    ): Response<GetWeatherResponse>


    companion object {

        fun create(context : Context): ApiService {

            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(OAuthInterceptor(PrivateSharedPrefManager(context)))
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
