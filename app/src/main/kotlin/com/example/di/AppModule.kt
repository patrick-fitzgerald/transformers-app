package com.example.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.BuildConfig
import com.example.api.JwtInterceptor
import com.example.api.TransformersApi
import com.example.db.TransformersDatabase
import com.example.repository.TransformersRepository
import com.example.ui.home.HomeViewModel
import com.example.ui.splash.SplashViewModel
import com.example.ui.transformer.TransformerViewModel
import com.example.util.PreferenceHelper
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    single { SplashViewModel(get(), get()) }
    single { HomeViewModel(get()) }
    single { TransformerViewModel(get()) }
}

val networkModule = module {

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    fun provideOkHttpClient(): OkHttpClient {

        val jwtInterceptor = JwtInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder()
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(jwtInterceptor)
            .build()
    }

    fun provideTransformersApi(retrofit: Retrofit): TransformersApi =
        retrofit.create(TransformersApi::class.java)

    factory { provideOkHttpClient() }
    factory { provideTransformersApi(get()) }
    single { provideRetrofit(get()) }
}

val repositoryModule = module {
    single { TransformersRepository(get(), get()) }
}

val prefsModule = module {
    single { PreferenceHelper(get()) }
}

val dbModule = module {

    single {
        Room.databaseBuilder(get(), TransformersDatabase::class.java, "transformers_db")
            .build()
    }
}


val glideModule = module {

    fun provideRequestManager(
        application: Application
    ): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(RequestOptions())
    }
    single { provideRequestManager(androidApplication()) }
}

val appModules =
    listOf(viewModelModule, networkModule, repositoryModule, prefsModule, dbModule, glideModule)
