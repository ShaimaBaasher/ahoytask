package com.shaima.ahoytask.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaima.ahoytask.domain.settings.usecase.ChangeTemperatureUnitUseCase
import com.shaima.ahoytask.domain.settings.usecase.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    val changeTemperatureUnitUseCase: ChangeTemperatureUnitUseCase,
    val getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    private val _viewState = MutableLiveData<Boolean>()
    val viewState: LiveData<Boolean>
        get() = _viewState

    init {
        _viewState.value = getSettingsUseCase.invoke()
    }

    fun setTemperatureUnit(unit: Boolean) {
        changeTemperatureUnitUseCase(unit)
        _viewState.value = getSettingsUseCase.invoke()
    }

}
