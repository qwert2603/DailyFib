package com.qwert2603.daily_fib.main_activity.adapter

import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.qwert2603.daily_fib.R
import com.qwert2603.daily_fib.main_activity.LoadingItem
import com.qwert2603.daily_fib.util.inflate
import com.qwert2603.daily_fib.util.setVisible
import kotlinx.android.synthetic.main.item_loading.view.*

class LoadingDelegateAdapter : DelegateAdapter<LoadingItem, LoadingVH> {
    override fun createVH(parent: ViewGroup): LoadingVH {
        return LoadingVH(parent.inflate(R.layout.item_loading))
    }

    override fun bind(viewHolder: LoadingVH, item: LoadingItem) {
        viewHolder.bind(item)
    }
}

class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(loadingItem: LoadingItem) = with(itemView) {
        TransitionManager.beginDelayedTransition(item_loading_comments_FrameLayout)
        item_loading_progressBar.setVisible(loadingItem.loading)
        item_loading_error_TextView.setVisible(loadingItem.error != null)
    }
}
