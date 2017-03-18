package com.qwert2603.daily_fib

data class Post(
        val id: Long,
        val text: String,
        val date: Long,
        val comments: Count,
        val likes: Count,
        val reposts: Count
)

data class Response(
        val response: List<Post>
)

data class Count(val count: Int)