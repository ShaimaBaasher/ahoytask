package com.shaima.ahoytask.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.shaima.ahoytask.data.home.MainData
import kotlinx.coroutines.flow.*
import com.shaima.ahoytask.domain.core.BaseResult
import com.shaima.ahoytask.domain.home.usecase.GetPlacesUseCase
import com.shaima.ahoytask.domain.home.usecase.StoreWeatherData
import com.shaima.ahoytask.domain.settings.usecase.GetSettingsUseCase
import com.shaima.ahoytask.domain.state.HomeFavFragmentState
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import com.ydhnwb.cleanarchitectureexercise.domain.login.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getWeatherUseCase: GetWeatherUseCase,
                                        private val storeWeatherData: StoreWeatherData,
                                        private val getPlacesUseCase: GetPlacesUseCase,
                                        private val getSettingsUseCase: GetSettingsUseCase
): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val state = MutableStateFlow<HomeFavFragmentState>(HomeFavFragmentState.Init)
    val mStateFav: StateFlow<HomeFavFragmentState> get() = state

    private val weather = MutableStateFlow<List<MainData>>(mutableListOf())
    val mWeather: StateFlow<List<MainData>> get() = weather

    private val _viewState = MutableLiveData<Boolean>()
    val viewStatse: LiveData<Boolean>
        get() = _viewState


    fun getSettings() {
        _viewState.value = getSettingsUseCase.invoke()
    }
    private fun setLoading() {
        state.value = HomeFavFragmentState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = HomeFavFragmentState.IsLoading(false)
    }

    private fun showToast(message: String) {
        state.value = HomeFavFragmentState.ShowToast(message)
    }

    fun getWeatherData(lat : String, lon : String) {
        viewModelScope.launch {
            getWeatherUseCase.execute(lat, lon)
                .onStart {
                    setLoading()
                }
                .catch {
                    hideLoading()
                    showToast(it.message.toString())
                }
                .collect {
                    hideLoading()
                    when(it) {
                        is BaseResult.Success -> state.value = HomeFavFragmentState.SuccessGetWeather(it.data)
                        is BaseResult.ErrorMsg -> state.value = HomeFavFragmentState.ShowToast(it.msg)
                        is BaseResult.Error ->  state.value = HomeFavFragmentState.ShowToast(it.exception.localizedMessage)
                    }
                }
        }
    }

    fun getWeatherByName(cityName : String) {
        viewModelScope.launch {
            getWeatherUseCase.getWeatherByName(cityName)
                .onStart {
                    setLoading()
                }
                .catch {
                    hideLoading()
                    showToast(it.message.toString())
                }
                .collect {
                    hideLoading()
                    when(it) {
                        is BaseResult.Success -> state.value = HomeFavFragmentState.SuccessGetWeather(it.data)
                        is BaseResult.ErrorMsg -> state.value = HomeFavFragmentState.ShowToast(it.msg)
                        is BaseResult.Error ->  state.value = HomeFavFragmentState.ShowToast(it.exception.localizedMessage)
                    }
                }
        }
    }

    fun storePlace(weatherEntity: WeatherEntity) = viewModelScope.launch {
        storeWeatherData(
            WeatherEntity(
                weatherEntity.list,
                weatherEntity.city,
            )
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    val viewState = getPlacesUseCase()
        .map { result ->
            when (result) {
                is BaseResult.Success -> HomeFavFragmentState.SuccessGetFromDb(result.data.map { it })
                is BaseResult.ErrorMsg -> result.msg?.let { HomeFavFragmentState.ShowToast(it) }
                is BaseResult.Error -> Log.d("exceptionMessage", result.exception.localizedMessage)
            }
        }
        .asLiveData(viewModelScope.coroutineContext)
}

