package com.ydhnwb.cleanarchitectureexercise.data.login.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.shaima.ahoytask.BuildConfig
import com.shaima.ahoytask.data.APIs
import com.shaima.ahoytask.data.database.WeatherDao
import com.shaima.ahoytask.data.home.*
import com.shaima.ahoytask.domain.home.WeatherRepository
import com.shaima.ahoytask.domain.core.BaseResult
import com.shaima.api.repository.Utils
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.fromWeather
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.toPlace
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.flow.*

const val API_KEY = BuildConfig.API_KEY

class WeatherRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherDao: WeatherDao,
    private val api: APIs
) : WeatherRepository {

    override suspend fun getWeatherByLocation(
        lat: String,
        lon: String
    ): Flow<BaseResult<WeatherEntity>> {
        return flow {
            try {
                val response = api.getWeatherByLocation(lat, lon, "metric", API_KEY)

                Log.d("responseFromServer", Gson().toJson(response.body()))
                val body = response.body()
                if (body?.cod.equals("200")) {
                    val weatherResponse = mutableListOf<WeatherResponse>()
                    val weatherList = mutableListOf<Weather>()

                    body?.list?.forEach {
                        weatherList.add(Weather(it.weather?.get(0)?.main, it.weather?.get(0)?.icon))
                        weatherResponse.add(
                            WeatherResponse(
                                MainData(it.main?.temp,
                                    it.main?.temp?.let { it1 -> Utils.convertCelciusToFahrenheit(it1).toString() }, it.main?.humidity),
                                WindData(it.wind?.speed),
                                it.dt_txt!!, weatherList)
                        )
                    }
                    Log.d("responseFromServerFS", Gson().toJson(weatherResponse))

                    emit(
                        BaseResult.Success(
                            WeatherEntity(
                                weatherResponse,
                                CityResponse(body?.city?.id, body?.city?.name),
                            )
                        )
                    )
                } else
                    emit(BaseResult.ErrorMsg(body!!.message))
            } catch (e: Exception) {
                emit(Utils.resolveError(e))
            }
        }
    }

    override suspend fun getWeatherByCityName(cityName: String): Flow<BaseResult<WeatherEntity>> {
        return flow {
            try {
                val response = api.getWeatherByCityName(cityName, "metric", API_KEY)
                Log.d("responseFromServerF", Gson().toJson(response.body()))

                val body = response.body()
                if (body?.cod.equals("200") || body != null) {
                    val weatherResponse = mutableListOf<WeatherResponse>()
                    val weatherList = mutableListOf<Weather>()

                    body?.list?.forEach {
                        weatherList.add(Weather(it.weather?.get(0)?.main, it.weather?.get(0)?.icon))
                        weatherResponse.add(
                            WeatherResponse(
                                MainData(it.main?.temp,
                                    it.main?.temp?.let { it1 -> Utils.convertCelciusToFahrenheit(it1).toString() }, it.main?.humidity),
                                WindData(it.wind?.speed),
                                it.dt_txt!!, weatherList)
                        )
                    }

                    Log.d("responseFromServerSe", Gson().toJson(weatherResponse))

                    emit(
                        BaseResult.Success(
                            WeatherEntity(
                                weatherResponse,
                                CityResponse(body?.city?.id, body?.city?.name)
                            )
                        )
                    )
                } else {
                    emit(BaseResult.ErrorMsg(response.body()!!.message))
                }
            } catch (e: Exception) {
                emit(BaseResult.ErrorMsg("Something Went Wrong"))
            }
        }
    }

    override suspend fun storeWeather(weatherEntity: WeatherEntity) {
        val placeEntity = fromWeather(weatherEntity)
        Log.d("placeEntity", Gson().toJson(placeEntity))
        weatherDao.insert(placeEntity)
    }

    override  fun observePlaces(): Flow<BaseResult<List<WeatherEntity>>> {
        return weatherDao.loadAll()
            .map { weatherEntities -> BaseResult.Success(weatherEntities.map { it.toPlace() }) }
    }

}
