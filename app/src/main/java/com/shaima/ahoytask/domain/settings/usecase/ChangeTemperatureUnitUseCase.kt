package com.shaima.ahoytask.domain.settings.usecase

import com.shaima.ahoytask.domain.settings.SettingRepository
import javax.inject.Inject

class ChangeTemperatureUnitUseCase @Inject constructor(private val settingsRepository: SettingRepository) {
    operator fun invoke(isCelsius: Boolean) {
        return settingsRepository.changeTemperatureUnit(isCelsius)
    }
}