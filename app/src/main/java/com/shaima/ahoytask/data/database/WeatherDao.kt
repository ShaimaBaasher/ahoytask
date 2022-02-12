package com.shaima.ahoytask.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntityDAO
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherEntity: WeatherEntityDAO)

    @Query("SELECT * FROM weather_table")
    fun loadAll(): Flow<List<WeatherEntityDAO>>
}
