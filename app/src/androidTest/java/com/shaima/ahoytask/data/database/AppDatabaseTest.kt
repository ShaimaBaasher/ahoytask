package com.shaima.ahoytask.data.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.shaima.ahoytask.data.home.*
import com.shaima.ahoytask.domain.core.BaseResult
import com.shaima.ahoytask.domain.state.HomeFavFragmentState
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntityDAO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var db : AppDatabase
    private lateinit var dao : WeatherDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.weatherDao()
    }

    @Test
    fun writeAndReadWeather() = runBlocking {
        val weatherResponseList = mutableListOf<WeatherResponse>()
        val weatherModelList = mutableListOf<Weather>()
        val cityResponse = CityResponse("1", "CityName")
        weatherModelList.add(Weather("200", ""))
        weatherResponseList.add(
            WeatherResponse(MainData("12", "25", "13"),
                WindData("20"), "12:00 PM", weatherModelList))
        val weatherEntity = WeatherEntityDAO(0, weatherResponseList, cityResponse)

        dao.insert(weatherEntity = weatherEntity)

        val weathers = dao.loadAll().map {
            it.map { it }
        }.asLiveData()
        Log.d("weathers", Gson().toJson(weathers.value))
        assertNotNull(weathers.value)
    }

    @After
    fun closeDb() {
        db.close()
    }

}