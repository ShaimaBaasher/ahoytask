package com.shaima.ahoytask.domain.settings

import com.shaima.ahoytask.data.home.CityResponse
import com.shaima.ahoytask.data.home.WeatherResponse
import com.shaima.ahoytask.domain.core.BaseResult
import com.shaima.ahoytask.utils.WrappedListResponse
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntityDAO
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun changeTemperatureUnit(isCelsius: Boolean)
    fun getTemp(): Boolean
}