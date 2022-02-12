package com.shaima.ahoytask.di

import android.content.Context
import com.shaima.ahoytask.data.APIs
import com.shaima.ahoytask.data.database.WeatherDao
import com.shaima.ahoytask.domain.home.WeatherRepository
import com.shisheo.clean_3.data.NetworkModule
import com.ydhnwb.cleanarchitectureexercise.data.login.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class HomeModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(@ApplicationContext context: Context,
                                 weatherDao: WeatherDao, api: APIs) : WeatherRepository {
        return WeatherRepositoryImpl(context, weatherDao, api)
    }
    
}