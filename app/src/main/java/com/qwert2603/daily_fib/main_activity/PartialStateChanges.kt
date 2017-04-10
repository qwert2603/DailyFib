package com.qwert2603.daily_fib.main_activity

sealed class PartialStateChanges {
    class FirstPageLoading : PartialStateChanges()
    class FirstPageError(val error: Throwable) : PartialStateChanges()
    class FirstPageLoaded(val items: List<Item>) : PartialStateChanges()

    class NextPageLoading : PartialStateChanges()
    class NextPageError(val error: Throwable) : PartialStateChanges()
    class NextPageLoaded(val items: List<Item>) : PartialStateChanges()

    class RefreshPageLoading : PartialStateChanges()
    class RefreshPageError(val error: Throwable) : PartialStateChanges()
    class RefreshPageLoaded(val items: List<Item>) : PartialStateChanges()

    class CommentsLoading(val postId: Long) : PartialStateChanges()
    class CommentsError(val postId: Long, val error: Throwable) : PartialStateChanges()
    class CommentsLoaded(val postId: Long, val items: List<Item>) : PartialStateChanges()
    class CommentsClose(val postId: Long) : PartialStateChanges()
}