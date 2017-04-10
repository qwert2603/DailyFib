package com.qwert2603.daily_fib.main_activity

data class MainActivityViewState(
        val firstPageLoading: Boolean,
        val firstPageError: Throwable?,
        val nextPageLoading: Boolean,
        val nextPageError: Throwable?,
        val refreshLoading: Boolean,
        val refreshError: Throwable?,
        val items: List<Item>?
)

interface Item {
    companion object {
        const val VIEW_TYPE_POST = 1
        const val VIEW_TYPE_COMMENT = 2
        const val VIEW_TYPE_LOADING = 3
        const val VIEW_TYPE_LOADING_COMMENTS = 4
        const val VIEW_TYPE_NO_COMMENTS = 5
    }

    val viewType: Int
    val id: Long
}

data class PostItem(
        override val id: Long,
        val text: String,
        val date: Long,
        val comments: Int,
        val likes: Int,
        val reposts: Int,
        val views: Int,
        val number: Int,
        val commentsOpened: Boolean = false
) : Item {
    override val viewType = Item.VIEW_TYPE_POST
}

data class CommentItem(
        override val id: Long,
        val author: CommentAuthor,
        val text: String,
        val date: Long
) : Item {
    override val viewType = Item.VIEW_TYPE_COMMENT
}

data class CommentAuthor(
        val id: Long,
        val name: String,
        val photo: String
)

data class LoadingItem(
        val loading: Boolean,
        val error: Throwable?
) : Item {
    override val viewType = Item.VIEW_TYPE_LOADING
    override val id = Long.MAX_VALUE - 123287L
}

data class LoadingCommentsItem(
        val postItemId: Long,
        val loading: Boolean,
        val error: Throwable?
) : Item {
    override val viewType = Item.VIEW_TYPE_LOADING_COMMENTS
    override val id = postItemId shl 26
}

data class NoCommentsItem(
        val postItemId: Long
) : Item {
    override val viewType = Item.VIEW_TYPE_NO_COMMENTS
    override val id = (postItemId shl 26) + (1L shl 25)
}