package com.method.highpoint.network

import android.content.Context
import android.net.Uri.Builder
import android.util.Log
import com.google.gson.GsonBuilder
import com.method.highpoint.BuildConfig
import com.method.highpoint.utils.isOnline
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MyMarketApiClient {

    companion object {
        private var retrofit: Retrofit? = null

        fun getApiClient(context: Context): Retrofit {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val REWRITE_RESPONSE_INTERCEPTOR = Interceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                val cacheControl = originalResponse.header("Cache-Control")
                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                        "no-cache"
                    ) ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
                ) {
                    originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .addHeader("X-Api-Key", BuildConfig.X_API_KEY)
                        .header("Cache-Control", "public, max-age=" + 5000)
                        .build()
                } else {
                    originalResponse
                }
            }

            val REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                val cacheHeaderValue: String = if (isOnline(context)) { "public, max-age=2419200" } else "public, only-if-cached, max-stale=2419200"
                //val maxAge = 5
                originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control",  cacheHeaderValue)
                    .build()
            }

            val REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = Interceptor { chain ->
                var request: Request = chain.request().newBuilder().addHeader("X-Api-Key", BuildConfig.X_API_KEY).build()
                val cacheHeaderValue: String = if (isOnline(context)) { "public, max-age=2419200" } else "public, only-if-cached, max-stale=2419200"
                request = request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheHeaderValue)
                    .build()
                chain.proceed(request)
            }

            val cacheSize = (5 * 1024 * 1024).toLong()
            val myCache = Cache(context.cacheDir, cacheSize)

            val okHttpClient =
                OkHttpClient.Builder()
                    .cache(myCache)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .addNetworkInterceptor(networkInterceptor())
                    .addInterceptor(offlineInterceptor(context))
                    .build()
//                if (isOnline(context)) {
//                    OkHttpClient.Builder()
//                        .cache(myCache)
//                        .readTimeout(100, TimeUnit.SECONDS)
//                        .connectTimeout(100, TimeUnit.SECONDS)
//                        .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
//                        .addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)
//                        .build()
//                } else {
//                     OkHttpClient.Builder()
//                        .readTimeout(100, TimeUnit.SECONDS)
//                        .connectTimeout(100, TimeUnit.SECONDS)
//                        .addInterceptor { apiKeyAsHeader(it) }
//                        .build()
//                }

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.HIGHPOINT_MARKET_API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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

        private fun offlineInterceptor(context: Context): Interceptor {
            return Interceptor { chain ->
                Log.d("TAG", "offline interceptor: called.")
                var request = chain.request()

                // prevent caching when network is on. For that we use the "networkInterceptor"
                if (!isOnline(context)) {
                    val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()
                    request = request.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .addHeader("X-Api-Key", BuildConfig.X_API_KEY)
                        .cacheControl(cacheControl)
                        .build()
                } else {
                    request = request.newBuilder()
                        .addHeader("X-Api-Key", BuildConfig.X_API_KEY)
                        .build()
                }
                chain.proceed(request)
            }
        }

        private fun networkInterceptor(): Interceptor {
            return Interceptor { chain ->
                Log.d("TAG", "network interceptor: called.")
                val response = chain.proceed(chain.request())
                val cacheControl = CacheControl.Builder()
                    .maxAge(5, TimeUnit.SECONDS)
                    .build()
                response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .addHeader("X-Api-Key", BuildConfig.X_API_KEY)
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
        }
    }
}