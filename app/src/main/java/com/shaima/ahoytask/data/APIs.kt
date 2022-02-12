package com.shaima.ahoytask.data

import com.google.gson.JsonObject
import com.shaima.ahoytask.data.home.CityResponse
import com.shaima.ahoytask.data.home.WeatherResponse
import com.shaima.ahoytask.utils.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIs {
    @GET("forecast")
    suspend fun getWeatherByLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<WrappedListResponse>

    @GET("forecast")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<WrappedListResponse>
}