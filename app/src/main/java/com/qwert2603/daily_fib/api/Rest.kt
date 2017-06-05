package com.qwert2603.daily_fib.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Rest {

    companion object {
        const val TOKEN = "0bff5dfe0bff5dfe0b4de75fcb0ba5d9a200bff0bff5dfe5332485e8c9a931502ecc84a"
        const val BASE_URL = "https://api.vk.com/method/"
    }

    @GET("wall.get")
    fun getPosts(
            @Query("offset") offset: Int,
            @Query("count") count: Int = 17,
            @Query("domain") domain: String = "daily_fib",
            @Query("access_token") access_token: String = TOKEN,
            @Query("v") v: String = "5.63"
    ): Single<ResponsePostsObject>

    @GET("wall.getComments")
    fun getComments(
            @Query("post_id") post_id: Int,
            @Query("offset") offset: Int = 0,
            @Query("count") count: Int = 100,
            @Query("owner_id") owner_id: Int = -141511386,
            @Query("extended") extended: Int = 1,
            @Query("access_token") access_token: String = TOKEN,
            @Query("v") v: String = "5.63"
    ): Single<ResponseCommentsObject>
}