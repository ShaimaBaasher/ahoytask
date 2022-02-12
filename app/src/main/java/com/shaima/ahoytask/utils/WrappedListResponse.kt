package com.shaima.ahoytask.utils

import com.google.gson.annotations.SerializedName
import com.shaima.ahoytask.data.home.CityResponse
import com.shaima.ahoytask.data.home.WeatherResponse

data class WrappedListResponse(
    @SerializedName("cod") var cod : String,
    @SerializedName("message") var message : String,
    @SerializedName("list") var list : List<WeatherResponse>? = null,
    @SerializedName("city") var city : CityResponse? = null,
)