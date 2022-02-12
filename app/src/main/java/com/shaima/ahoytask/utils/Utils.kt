package com.shaima.api.repository

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.shaima.ahoytask.R
import com.shaima.ahoytask.domain.core.BaseResult
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*


object Utils {

    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    fun resolveError(e: Exception): BaseResult.Error {
        var error = e
        when (e) {
            is SocketTimeoutException -> {
                error = NetworkErrorException(errorMessage = "connection error!")
            }
            is ConnectException -> {
                error = NetworkErrorException(errorMessage = "no internet access!")
            }
            is UnknownHostException -> {
                error = NetworkErrorException(errorMessage = "no internet access!")
            }
        }

        if (e is HttpException) {
            when (e.code()) {
                502 -> {
                    error = NetworkErrorException(e.code(), "internal error!")
                }
                401 -> {
                    throw AuthenticationException("authentication error!")
                }
                400 -> {
                    error = NetworkErrorException.parseException(e)
                }
            }
        }
        return BaseResult.Error(error)
    }

    fun convertDate(input: String?): String? {
        val inFormat = SimpleDateFormat("yyyy-dd-MM hh:mm:ss")
        val date: Date = inFormat.parse(input)
        val outFormat = SimpleDateFormat("EEEE")
        val goal: String = outFormat.format(date)
        return goal
    }

    fun checkValid(input: String?): Boolean {
        var boolean = true
        if (input?.isEmpty() == true) {
            boolean = false
        }

        if (input?.length!! < 4) {
            boolean = false
        }
        return boolean
    }

    fun convertTime(input: String?): String? {
        val inFormat = SimpleDateFormat("yyyy-dd-MM hh:mm:ss")
        val date: Date = inFormat.parse(input)
        val outFormat = SimpleDateFormat("hh:mm aaa")
        val goal: String = outFormat.format(date)
        return goal
    }

    // Converts to fahrenheit
    fun convertCelciusToFahrenheit(a: String): Float {
        Log.d("celsiusA", a)
        val celsius = java.lang.String.valueOf(a).toFloat()
        val result = (celsius * 9 / 5) + 32
        Log.d("celsius", result.toString())
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDuration(): Duration {
        val alarmTime = LocalTime.of(3, 45)
        var now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        val nowTime = now.toLocalTime()
        // if same time, schedule for next day as well
        // if today's time had passed, schedule for next day
        if (nowTime == alarmTime || nowTime.isAfter(alarmTime)) {
            now = now.plusDays(1)
        }
        now = now.withHour(alarmTime.hour)
            .withMinute(alarmTime.minute) // .withSecond(alarmTime.second).withNano(alarmTime.nano)
        val duration = Duration.between(LocalDateTime.now(), now)
        return duration
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null
    }

}
