package com.ydhnwb.cleanarchitectureexercise.domain.login.usecase

import com.shaima.ahoytask.domain.home.WeatherRepository
import com.shaima.ahoytask.domain.core.BaseResult
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun execute(lat : String, lon : String): Flow<BaseResult<WeatherEntity>>  {
        return weatherRepository.getWeatherByLocation(lat, lon)
    }
}