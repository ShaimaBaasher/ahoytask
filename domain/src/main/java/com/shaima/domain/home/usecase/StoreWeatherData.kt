package com.shaima.ahoytask.domain.home.usecase

import com.shaima.ahoytask.domain.home.WeatherRepository
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import javax.inject.Inject

class StoreWeatherData @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(entity: WeatherEntity) {
        return weatherRepository.storeWeather(entity)
    }
}