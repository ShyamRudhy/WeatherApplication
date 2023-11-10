package com.shyam.weatherapp.data.api.model

import com.shyam.weatherapp.data.model.Clouds
import com.shyam.weatherapp.data.model.Coord
import com.shyam.weatherapp.data.model.Main
import com.shyam.weatherapp.data.model.Rain
import com.shyam.weatherapp.data.model.Sys
import com.shyam.weatherapp.data.model.Weather
import com.shyam.weatherapp.data.model.Wind

data class GetWeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)