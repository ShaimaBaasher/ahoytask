package com.shisheo.clean_3.data

import android.content.Context
import com.google.gson.GsonBuilder
import com.shaima.ahoytask.App
import com.shaima.ahoytask.BuildConfig
import com.shaima.ahoytask.data.APIs
import com.shaima.ahoytask.utils.Constants
import com.shisheo.clean_3.data.NetworkModule.component.BASE_URL

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class NetworkModule {

    object component {
        const val BASE_URL = BuildConfig.API_BASE_URL
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            client(okHttp)
            baseUrl(BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttp() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            addInterceptor(logging)
        }.build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit) : APIs {
        return retrofit.create(APIs::class.java)
    }


}
