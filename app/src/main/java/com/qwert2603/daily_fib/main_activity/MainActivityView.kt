package com.qwert2603.daily_fib.main_activity

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainActivityView : MvpView {

    fun loadFirstPage(): Observable<Any>
    fun reloadFirstPage(): Observable<Any>
    fun loadNextPage(): Observable<Any>
    fun refresh(): Observable<Any>
    fun openComments(): Observable<Long>
    fun closeComments(): Observable<Long>

    fun render(mainActivityViewState: MainActivityViewState)
}