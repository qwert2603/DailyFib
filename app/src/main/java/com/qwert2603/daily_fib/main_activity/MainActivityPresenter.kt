package com.qwert2603.daily_fib.main_activity

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.qwert2603.daily_fib.api.DataManager
import io.reactivex.Observable

class MainActivityPresenter : MviBasePresenter<MainActivityView, MainActivityViewState>() {

    val dataManager = DataManager()

    override fun bindIntents() {
        val observable = Observable.merge<PartialStateChanges>(listOf(
                intent { it.loadFirstPage() }
                        .flatMap {
                            dataManager.loadNewestPosts()
                                    .toObservable()
                                    .map { PartialStateChanges.FirstPageLoaded(it) }
                                    .cast(PartialStateChanges::class.java)
                                    .onErrorReturn { PartialStateChanges.FirstPageError(it) }
                                    .startWith(PartialStateChanges.FirstPageLoading())
                        },
                intent { it.reloadFirstPage() }
                        .flatMap {
                            dataManager.loadNewestPosts()
                                    .toObservable()
                                    .map { PartialStateChanges.FirstPageLoaded(it) }
                                    .cast(PartialStateChanges::class.java)
                                    .onErrorReturn { PartialStateChanges.FirstPageError(it) }
                                    .startWith(PartialStateChanges.FirstPageLoading())
                        },
                intent { it.loadNextPage() }
                        .flatMap {
                            dataManager.loadNextPosts()
                                    .toObservable()
                                    .map { PartialStateChanges.NextPageLoaded(it) }
                                    .cast(PartialStateChanges::class.java)
                                    .onErrorReturn { PartialStateChanges.NextPageError(it) }
                                    .startWith(PartialStateChanges.NextPageLoading())
                        },
                intent { it.refresh() }
                        .flatMap {
                            dataManager.loadNewestPosts()
                                    .toObservable()
                                    .map { PartialStateChanges.RefreshPageLoaded(it) }
                                    .cast(PartialStateChanges::class.java)
                                    .onErrorReturn { PartialStateChanges.RefreshPageError(it) }
                                    .startWith(PartialStateChanges.RefreshPageLoading())
                        },
                intent { it.openComments() }
                        .flatMap { postId ->
                            dataManager.loadComments(postId)
                                    .toObservable()
                                    .map { PartialStateChanges.CommentsLoaded(postId, it) }
                                    .cast(PartialStateChanges::class.java)
                                    .onErrorReturn { PartialStateChanges.CommentsError(postId, it) }
                                    .startWith(PartialStateChanges.CommentsLoading(postId))
                        },
                intent { it.closeComments() }.map { PartialStateChanges.CommentsClose(it) }
        ))

        val initialValue = MainActivityViewState(false, null, false, null, false, null, null)
        subscribeViewState(observable.scan(initialValue, this::viewStateReducer), MainActivityView::render)
    }

    fun viewStateReducer(viewState: MainActivityViewState, changes: PartialStateChanges): MainActivityViewState {
        return when (changes) {
            is PartialStateChanges.FirstPageLoading -> viewState.copy(
                    firstPageLoading = true,
                    firstPageError = null
            )
            is PartialStateChanges.FirstPageError -> viewState.copy(
                    firstPageLoading = false,
                    firstPageError = changes.error
            )
            is PartialStateChanges.FirstPageLoaded -> viewState.copy(
                    firstPageLoading = false,
                    firstPageError = null,
                    items = changes.items
            )
            is PartialStateChanges.NextPageLoading -> viewState.copy(
                    nextPageLoading = true,
                    nextPageError = null,
                    items = viewState.items!! + listOf(LoadingItem(true, null))
            )
            is PartialStateChanges.NextPageError -> viewState.copy(
                    nextPageLoading = false,
                    nextPageError = changes.error,
                    items = viewState.items!!.filter { it !is LoadingItem }
            )
            is PartialStateChanges.NextPageLoaded -> viewState.copy(
                    nextPageLoading = false,
                    nextPageError = null,
                    items = viewState.items!!.filter { it !is LoadingItem } + changes.items
            )
            is PartialStateChanges.RefreshPageLoading -> viewState.copy(
                    refreshLoading = true,
                    refreshError = null
            )
            is PartialStateChanges.RefreshPageError -> viewState.copy(
                    refreshLoading = false,
                    refreshError = changes.error
            )
            is PartialStateChanges.RefreshPageLoaded -> viewState.copy(
                    refreshLoading = false,
                    refreshError = null,
                    items = changes.items
            )
            is PartialStateChanges.CommentsLoading -> viewState.copy(
                    items = viewState.items?.setCommentItems(changes.postId, listOf(LoadingCommentsItem(changes.postId, true, null)))
            )
            is PartialStateChanges.CommentsError -> viewState.copy(
                    items = viewState.items?.setCommentItems(changes.postId, listOf(LoadingCommentsItem(changes.postId, false, changes.error)))
            )
            is PartialStateChanges.CommentsLoaded -> viewState.copy(
                    items = viewState.items?.setCommentItems(changes.postId, if (changes.items.isNotEmpty()) changes.items else listOf(NoCommentsItem(changes.postId)))
            )
            is PartialStateChanges.CommentsClose -> viewState.copy(
                    items = viewState.items?.setCommentItems(changes.postId, emptyList())
            )
        }
    }

    fun List<Item>.setCommentItems(postId: Long, commentItems: List<Item>): List<Item> {
        val postIndex = indexOfFirst { (it as? PostItem)?.id == postId }
        if (postIndex < 0) return this
        var nextPostIndex = subList(postIndex + 1, size).indexOfFirst { it is PostItem }
        if (nextPostIndex < 0) nextPostIndex = size else nextPostIndex += postIndex + 1
        return subList(0, postIndex) + listOf((this[postIndex] as PostItem).copy(commentsOpened = commentItems.isNotEmpty())) + commentItems + subList(nextPostIndex, size)
    }
}