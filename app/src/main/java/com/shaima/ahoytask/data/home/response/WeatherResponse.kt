package com.shaima.ahoytask.data.home

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherResponse(
    @SerializedName("main") var main : MainData? = null,
    @SerializedName("wind") var wind : WindData? = null,
    @SerializedName("dt_txt") var dt_txt : String? = null,
    @SerializedName("weather") var weather : List<Weather>? = null,
    ) : Parcelable

@Parcelize
data class Weather(
    @SerializedName("main") var main : String? = null,
    @SerializedName("icon") var icon : String? = null, ) : Parcelable

@Parcelize
data class MainData(
    @SerializedName("temp") var temp : String? = null,
    var fahrenheit : String? = "0",
    @SerializedName("humidity") var humidity : String? = null, ) : Parcelable

@Parcelize
data class WindData(
    @SerializedName("speed") var speed : String? = null,
) : Parcelable

@Parcelize
data class CityResponse(
    @SerializedName("id") var id : String? = null,
    @SerializedName("name") var name : String? = null,
) : Parcelable

