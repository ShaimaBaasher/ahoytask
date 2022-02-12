package com.ydhnwb.cleanarchitectureexercise.domain.login.entity

import android.os.Parcelable
import androidx.room.*
import com.shaima.ahoytask.data.home.CityResponse
import com.shaima.ahoytask.data.home.MainData
import com.shaima.ahoytask.data.home.WeatherResponse
import com.shaima.ahoytask.utils.Converters
import kotlinx.parcelize.Parcelize

@Entity(tableName = "weather_table")
data class WeatherEntityDAO(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name="weatherResponse")
    var list: List<WeatherResponse>,
    @ColumnInfo(name="cityResponse")
    var city: CityResponse,
    var isFav: Boolean = false,
    )

@Parcelize
data class WeatherEntity(
    var list: List<WeatherResponse>,
    var city: CityResponse,
    var isFav: Boolean = false,
) : Parcelable

fun fromWeather(weather: WeatherEntity) = WeatherEntityDAO(0, weather.list, weather.city, weather.isFav)
fun WeatherEntityDAO.toPlace() = WeatherEntity(this.list, this.city, this.isFav)
