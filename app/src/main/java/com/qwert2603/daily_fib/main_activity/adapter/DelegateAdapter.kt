package com.qwert2603.daily_fib.main_activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.qwert2603.daily_fib.main_activity.Item

interface DelegateAdapter<in I : Item, VH : RecyclerView.ViewHolder> {
    fun createVH(parent: ViewGroup): VH
    fun bind(viewHolder: VH, item: I)
}
