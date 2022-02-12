package com.shaima.ahoytask.data.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import com.shaima.ahoytask.domain.core.BaseResult
import com.shaima.ahoytask.domain.settings.*
import javax.inject.Inject

class SettingsRepositoryImpl  @Inject constructor(private val sharedPreferences: SharedPreferences) :
    SettingRepository {

    private val celsiusKey = "CELSIUS_KEY"

    override fun changeTemperatureUnit(isCelsius: Boolean) {
        sharedPreferences.edit {
            putBoolean(celsiusKey, isCelsius)
        }
    }

    override fun getTemp(): Boolean {
        return sharedPreferences.getBoolean(celsiusKey, true)
    }

}