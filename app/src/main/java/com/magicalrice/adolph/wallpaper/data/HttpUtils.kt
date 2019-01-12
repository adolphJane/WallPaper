package com.magicalrice.adolph.wallpaper.data

import com.google.gson.GsonBuilder
import com.magicalrice.adolph.wallpaper.utils.LUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.util.concurrent.TimeUnit

object HttpUtils {
    fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        val logIntercepter = HttpLoggingInterceptor {
            try {
                LUtils.e("OkHttp----", it)
            } catch (e: UnsupportedEncodingException) {
                LUtils.e("OkHttp----", it)
            }
        }

        logIntercepter.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .readTimeout(HttpConstants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HttpConstants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(logIntercepter)
    }

    fun getRetrofitBuilder(baseUrl: String): Retrofit.Builder {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create()
        val okHttpClient = getOkHttpClientBuilder().build()
        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
    }

    fun getRetrofit(baseUrl: String): Retrofit {
        return getRetrofitBuilder(baseUrl).build()
    }
}