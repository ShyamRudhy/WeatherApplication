package com.shyam.weatherapp.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utils {

    companion object
    {

        fun date(): String {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            return sdf.format((Date()))
        }

        fun time(timeStamp : Long): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format((timeStamp*1000))
        }
        fun dayName(timeStamp : Long ): String {
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            return sdf.format((Date()))
        }
    }
}