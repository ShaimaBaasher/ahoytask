package com.shaima.ahoytask.domain.settings.usecase

import com.shaima.ahoytask.domain.settings.SettingRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(private val settingsRepository: SettingRepository) {

    operator fun invoke(): Boolean {
        return settingsRepository.getTemp()
    }
}
