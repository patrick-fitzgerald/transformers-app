package com.example.api

import com.example.util.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class JwtInterceptor : Interceptor, KoinComponent {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    private val prefs: PreferenceHelper by inject()

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")

        prefs.jwtToken?.let { jwtToken ->
            Timber.d("jwtToken: $jwtToken")
            requestBuilder.addHeader(AUTHORIZATION_HEADER, "Bearer $jwtToken")
        }

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}
