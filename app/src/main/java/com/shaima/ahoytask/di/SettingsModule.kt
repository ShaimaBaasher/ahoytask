package com.shaima.ahoytask.di

import android.content.Context
import com.shaima.ahoytask.data.APIs
import com.shaima.ahoytask.data.database.WeatherDao
import com.shaima.ahoytask.data.settings.SettingsRepositoryImpl
import com.shaima.ahoytask.domain.home.WeatherRepository
import com.shaima.ahoytask.domain.settings.SettingRepository
import com.ydhnwb.cleanarchitectureexercise.data.login.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SettingsModule {
    @Singleton
    @Provides
    fun provideSettingsModule(
        settingsRepository: SettingsRepositoryImpl
    ): SettingRepository =
        settingsRepository
}
