package com.may.newsapplication.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiFactory {

    companion object {
        private const val CONNECTION_TIMEOUT_SEC = 10L
        private const val BASE_URL="https://newsapi.org/v2/"
    }

    fun createApiClient() : IApiClient {
        return createRetrofit()
            .create(IApiClient::class.java)
    }

    private val gson = Gson()

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(createHttpClient())
            .build()

    }

    private fun createHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        builder.addInterceptor(loggingInterceptor)

        builder.connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        builder.readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        builder.writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)

        return builder.build()
    }

}