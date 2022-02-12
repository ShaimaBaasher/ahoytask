package com.shaima.ahoytask.domain.home

import com.shaima.ahoytask.data.home.CityResponse
import com.shaima.ahoytask.data.home.WeatherResponse
import com.shaima.ahoytask.domain.core.BaseResult
import com.shaima.ahoytask.utils.WrappedListResponse
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntityDAO
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherByLocation(lat: String, lon : String) :
            Flow<BaseResult<WeatherEntity>>
    suspend fun getWeatherByCityName(cityName : String) :
            Flow<BaseResult<WeatherEntity>>
    suspend fun storeWeather(weatherEntity: WeatherEntity)
    fun observePlaces(): Flow<BaseResult<List<WeatherEntity>>>
}