package com.qwert2603.daily_fib.api

data class ResponsePostsObject(
        val response: ResponsePosts
)

data class ResponsePosts(
        val count: Int,
        val items: List<Post>
)

data class Post(
        val id: Long,
        val text: String,
        val date: Long,
        val comments: Count,
        val likes: Count,
        val reposts: Count,
        val views: Count?
)

data class Count(val count: Int)