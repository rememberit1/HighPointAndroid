package com.method.highpoint.network

import com.google.gson.GsonBuilder
import com.method.highpoint.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofit: Retrofit ?= null

        fun getApiClient(): Retrofit {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor { apiKeyAsHeader(it) }
                .build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.HIGHPOINT_MARKET_API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!
        }

        private fun apiKeyAsHeader(it: Interceptor.Chain) = it.proceed(
            it.request()
                .newBuilder()
                .addHeader("X-Api-Key", BuildConfig.X_API_KEY)
                .build()
        )
    }
}