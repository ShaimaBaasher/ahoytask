package com.shaima.ahoytask.domain.home.usecase

import com.shaima.ahoytask.domain.home.WeatherRepository
import com.shaima.ahoytask.domain.core.BaseResult
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlacesUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    operator fun invoke():Flow<BaseResult<List<WeatherEntity>>> {
        return weatherRepository.observePlaces()
    }
}
