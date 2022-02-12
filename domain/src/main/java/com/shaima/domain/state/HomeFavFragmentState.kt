package com.shaima.ahoytask.domain.state

import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity

sealed class HomeFavFragmentState {
    object Init : HomeFavFragmentState()
    data class IsLoading(val isLoading : Boolean) : HomeFavFragmentState()
    data class ShowToast(val message : String) : HomeFavFragmentState()
    data class SuccessGetWeather(val weatherEntity: WeatherEntity) : HomeFavFragmentState()
    data class SuccessGetFromDb(val weatherEntity: List<WeatherEntity>) : HomeFavFragmentState()
}