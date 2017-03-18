package com.qwert2603.daily_fib

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.atconsulting.strizhi.util.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

class MainActivity : AppCompatActivity() {

    private val dataManager = DataManager()

    private val compositeDisposable = CompositeDisposable()

    val layoutManager = LinearLayoutManager(this)
    val adapter = PostsAdapter()

    var allPostsLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        posts_RecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        posts_RecyclerView.layoutManager = layoutManager
        posts_RecyclerView.adapter = adapter
        posts_RecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (loading_FrameLayout.visibility != View.VISIBLE && !allPostsLoaded && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    loadPosts(false)
                }
            }
        })

        posts_SwipeRefreshLayout.setOnRefreshListener { loadPosts(true) }

        loadPosts(false)
    }

    private fun loadPosts(reloadAll: Boolean) {
        if (!reloadAll && allPostsLoaded) return

        loading_FrameLayout.setVisible(true)
        if (reloadAll) {
            adapter.posts = emptyList()
            allPostsLoaded = false
        }
        val disposable = dataManager.getPosts(adapter.posts.size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { posts, throwable ->
                    posts?.let {
                        allPostsLoaded = it.response.isEmpty()
                        adapter.posts += it.response
                        loading_FrameLayout.setVisible(false)
                        posts_SwipeRefreshLayout.isRefreshing = false
                    }
                    throwable?.let {
                        LogUtils.e("error loading posts!!!", it)
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                        posts_SwipeRefreshLayout.isRefreshing = false
                    }
                }
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
