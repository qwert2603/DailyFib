package com.qwert2603.daily_fib.main_activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.atconsulting.strizhi.util.LogUtils
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.qwert2603.daily_fib.R
import com.qwert2603.daily_fib.main_activity.adapter.ItemsAdapter
import com.qwert2603.daily_fib.util.setVisible
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MviActivity<MainActivityView, MainActivityPresenter>(), MainActivityView {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        itemsAdapter = ItemsAdapter()

        items_RecyclerView.layoutManager = layoutManager
        items_RecyclerView.adapter = itemsAdapter
        items_RecyclerView.recycledViewPool.setMaxRecycledViews(Item.VIEW_TYPE_POST, 20)
        items_RecyclerView.recycledViewPool.setMaxRecycledViews(Item.VIEW_TYPE_COMMENT, 20)
        items_RecyclerView.recycledViewPool.setMaxRecycledViews(Item.VIEW_TYPE_NO_COMMENTS, 20)
        items_RecyclerView.recycledViewPool.setMaxRecycledViews(Item.VIEW_TYPE_LOADING, 20)
        items_RecyclerView.recycledViewPool.setMaxRecycledViews(Item.VIEW_TYPE_LOADING_COMMENTS, 20)
    }

    override fun createPresenter() = MainActivityPresenter()

    override fun loadFirstPage(): Observable<Any> = Observable.just(Any())

    override fun reloadFirstPage(): Observable<Any> = RxView.clicks(error_TextView)

    override fun loadNextPage(): Observable<Any> = RxRecyclerView.scrollStateChanges(items_RecyclerView)
            .filter { it == RecyclerView.SCROLL_STATE_IDLE }
            .filter { layoutManager.findLastVisibleItemPosition() == itemsAdapter.itemCount - 1 }
            .map { Any() }

    override fun refresh(): Observable<Any> = RxSwipeRefreshLayout.refreshes(swipeRefreshLayout)

    override fun openComments() = itemsAdapter.openCommentsObservable

    override fun closeComments() = itemsAdapter.closeCommentsObservable

    override fun render(mainActivityViewState: MainActivityViewState) {
//        TransitionManager.beginDelayedTransition(swipeRefreshLayout)
        progressBar.setVisible(mainActivityViewState.firstPageLoading)
        error_TextView.setVisible(mainActivityViewState.firstPageError != null)
        items_RecyclerView.setVisible(mainActivityViewState.items != null)
        mainActivityViewState.items?.let { itemsAdapter.items = it }
        swipeRefreshLayout.isRefreshing = mainActivityViewState.refreshLoading
        mainActivityViewState.refreshError?.let { Toast.makeText(this, R.string.refresh_error_text, Toast.LENGTH_SHORT).show() }

        mainActivityViewState.firstPageError?.let { LogUtils.e("firstPageError", it) }
        mainActivityViewState.nextPageError?.let { LogUtils.e("nextPageError", it) }
        mainActivityViewState.refreshError?.let { LogUtils.e("refreshError", it) }
    }
}
