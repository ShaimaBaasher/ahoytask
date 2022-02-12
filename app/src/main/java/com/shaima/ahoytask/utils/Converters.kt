package com.shaima.ahoytask.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shaima.ahoytask.data.home.CityResponse
import com.shaima.ahoytask.data.home.WeatherResponse
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun fromWeatherList(weather: List<WeatherResponse?>?): String? {
        if (weather == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<WeatherResponse?>?>() {}.type
        return gson.toJson(weather, type)
    }

    @TypeConverter
    fun toWeatherList(weather: String?): List<WeatherResponse>? {
        if (weather == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<WeatherResponse?>?>() {}.type
        return gson.fromJson<List<WeatherResponse>>(weather, type)
    }

    @TypeConverter
    fun fromCity(cityResponse: CityResponse): String? {
        if (cityResponse == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<CityResponse?>() {}.type
        return gson.toJson(cityResponse, type)
    }

    @TypeConverter
    fun toCityList(weather: String?): CityResponse? {
        if (weather == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<CityResponse?>() {}.type
        return gson.fromJson<CityResponse>(weather, type)
    }

}