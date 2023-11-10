package com.shyam.weatherapp.common

import androidx.annotation.IntDef

class Constants {

    companion object {

        @IntDef(
            REQUEST_NONE,
            REQUEST_RUNNING,
            REQUEST_SUCCEEDED,
            REQUEST_FAILED
        )
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class RequestState

        const val ACCESS_TOKEN = "TOKEN"

        const val REQUEST_NONE = 0
        const val REQUEST_RUNNING = 1
        const val REQUEST_SUCCEEDED = 2
        const val REQUEST_FAILED = 3
        const val SERVICE_OK_STATUS = "ok"
        const val SERVICE_ERROR_STATUS = "error"

       // const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY = "f4a1a66ab7c0725c0a4e7b83fadceb38"


        const val DATABASE_NAME = "WeatherDB"

    }

}