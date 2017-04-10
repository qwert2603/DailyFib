package com.qwert2603.daily_fib.api

data class ResponseCommentsObject(
        val response: ResponseComments
)

data class ResponseComments(
        val count: Int,
        val items: List<Comment>,
        val profiles: List<Profile>,
        val groups: List<Group>
)

data class Comment(
        val id: Long,
        val from_id: Long,
        val text: String,
        val date: Long
)

data class Profile(
        val id: Long,
        val first_name: String,
        val last_name: String,
        val photo_100: String
)

data class Group(
        val id: Long,
        val name: String,
        val photo_100: String
)