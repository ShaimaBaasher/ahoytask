package com.shaima.ahoytask.domain.settings


interface SettingRepository {
    fun changeTemperatureUnit(isCelsius: Boolean)
    fun getTemp(): Boolean
}