package com.qwert2603.daily_fib.api

import com.qwert2603.daily_fib.main_activity.CommentAuthor
import com.qwert2603.daily_fib.main_activity.CommentItem
import com.qwert2603.daily_fib.main_activity.PostItem
import com.qwert2603.daily_fib.util.LogUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DataManager {

    private val rest: Rest = Retrofit.Builder()
            .client(OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { LogUtils.d(it) }).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build())
            .baseUrl(Rest.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(Rest::class.java)

    private var loadedCount = 0//todo: to separated class

    fun loadNewestPosts(): Single<List<PostItem>> = rest.getPosts(0)
            .map { it.response }
            .map { (count, items) ->
                loadedCount = 0
                items
                        .mapIndexed { index, post ->
                            PostItem(
                                    post.id,
                                    post.text,
                                    post.date,
                                    post.comments.count,
                                    post.likes.count,
                                    post.reposts.count,
                                    post.views?.count ?: 0,
                                    count - loadedCount - index
                            )
                        }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { loadedCount += it.size }

    fun loadNextPosts(): Single<List<PostItem>> = rest.getPosts(loadedCount)
            .map { it.response }
            .map { (count, items) ->
                items
                        .mapIndexed { index, post ->
                            PostItem(
                                    post.id,
                                    post.text,
                                    post.date,
                                    post.comments.count,
                                    post.likes.count,
                                    post.reposts.count,
                                    post.views?.count ?: 0,
                                    count - loadedCount - index
                            )
                        }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { loadedCount += it.size }

    fun loadComments(postId: Long): Single<List<CommentItem>> = rest.getComments(postId.toInt())
            .map { it.response }
            .map { response ->
                val authors = mutableMapOf<Long, CommentAuthor>()
                val profiles = response.profiles.map { CommentAuthor(it.id, "${it.first_name} ${it.last_name}", it.photo_100) }
                val groups = response.groups.map { CommentAuthor(-1 * it.id, it.name, it.photo_100) }
                (profiles + groups).forEach { authors.put(it.id, it) }
                response.items.map { CommentItem(it.id, authors[it.from_id]!!, it.text, it.date) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}