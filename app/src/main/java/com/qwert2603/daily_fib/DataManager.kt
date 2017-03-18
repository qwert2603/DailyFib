package com.qwert2603.daily_fib

import com.atconsulting.strizhi.util.LogUtils
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DataManager : Rest by Retrofit.Builder()
        .client(OkHttpClient.Builder()
                .addInterceptor {
                    val response = it.proceed(it.request())
                    val body = response.body().string()
                    val s = "\"response\": ["
                    val b = body.indexOf(s) + s.length
                    val e = body.indexOf("{", b)
                    val newBody = if (e > 0) "${body.substring(0, b + 1)}${body.substring(e, body.length)}" else "{\"response\":[]}"
                    LogUtils.d("newBody $newBody")
                    response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(), newBody))
                            .build()
                }
                .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { LogUtils.d(it) }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build())
        .baseUrl(Rest.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(Rest::class.java)