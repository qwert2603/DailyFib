package com.qwert2603.daily_fib.main_activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.qwert2603.daily_fib.R
import com.qwert2603.daily_fib.main_activity.NoCommentsItem
import com.qwert2603.daily_fib.util.inflate

class NoCommentsDelegateAdapter : DelegateAdapter<NoCommentsItem, NoCommentsVH> {
    override fun createVH(parent: ViewGroup) = NoCommentsVH(parent.inflate(R.layout.item_no_comments))

    override fun bind(viewHolder: NoCommentsVH, item: NoCommentsItem) {
        // nth
    }
}

class NoCommentsVH(itemView: View) : RecyclerView.ViewHolder(itemView)
