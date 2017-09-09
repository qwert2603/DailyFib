package com.qwert2603.daily_fib.main_activity.adapter

import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.qwert2603.daily_fib.util.LogUtils
import com.qwert2603.daily_fib.R
import com.qwert2603.daily_fib.main_activity.LoadingCommentsItem
import com.qwert2603.daily_fib.util.inflate
import com.qwert2603.daily_fib.util.setVisible
import kotlinx.android.synthetic.main.item_loading.view.*

class LoadingCommentsDelegateAdapter : DelegateAdapter<LoadingCommentsItem, LoadingCommentsVH> {
    override fun createVH(parent: ViewGroup) = LoadingCommentsVH(parent.inflate(R.layout.item_loading))

    override fun bind(viewHolder: LoadingCommentsVH, item: LoadingCommentsItem) {
        viewHolder.bind(item)
    }
}

class LoadingCommentsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var errorClickListener: ((Long) -> Unit)? = null
    var postItemId: Long = 0

    init {
        itemView.item_loading_error_TextView.setOnClickListener { errorClickListener?.invoke(postItemId) }
    }

    fun bind(loadingCommentsItem: LoadingCommentsItem) = with(itemView) {
        this@LoadingCommentsVH.postItemId = loadingCommentsItem.postItemId
        TransitionManager.beginDelayedTransition(item_loading_comments_FrameLayout)
        item_loading_progressBar.setVisible(loadingCommentsItem.loading)
        item_loading_error_TextView.setVisible(loadingCommentsItem.error != null)
        loadingCommentsItem.error?.let { LogUtils.e("loadingCommentsItem.error", it) }
    }
}
