package com.shaima.ahoytask.domain.home.usecase

import com.shaima.ahoytask.domain.core.BaseResult
import com.shaima.ahoytask.domain.home.WeatherRepository
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherByName @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun invoke(cityName : String): Flow<BaseResult<WeatherEntity>>  {
        return weatherRepository.getWeatherByCityName(cityName)
    }
}

