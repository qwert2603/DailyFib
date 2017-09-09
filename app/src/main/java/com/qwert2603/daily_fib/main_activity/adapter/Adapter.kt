package com.qwert2603.daily_fib.main_activity.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.qwert2603.daily_fib.main_activity.Item
import io.reactivex.subjects.PublishSubject

class ItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @Suppress("UNCHECKED_CAST")
    private val delegateAdapters = mapOf(
            Pair(Item.VIEW_TYPE_POST, PostDelegateAdapter() as DelegateAdapter<Item, RecyclerView.ViewHolder>),
            Pair(Item.VIEW_TYPE_COMMENT, CommentDelegateAdapter() as DelegateAdapter<Item, RecyclerView.ViewHolder>),
            Pair(Item.VIEW_TYPE_LOADING, LoadingDelegateAdapter() as DelegateAdapter<Item, RecyclerView.ViewHolder>),
            Pair(Item.VIEW_TYPE_LOADING_COMMENTS, LoadingCommentsDelegateAdapter() as DelegateAdapter<Item, RecyclerView.ViewHolder>),
            Pair(Item.VIEW_TYPE_NO_COMMENTS, NoCommentsDelegateAdapter() as DelegateAdapter<Item, RecyclerView.ViewHolder>)
    )

    var items: List<Item> = emptyList()
        set(value) {
            val prev = field
            field = value
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = prev.size
                override fun getNewListSize() = field.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = prev[oldItemPosition].id == field[newItemPosition].id
                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = prev[oldItemPosition] == field[newItemPosition]
            }).dispatchUpdatesTo(this)
        }

    val openCommentsObservable: PublishSubject<Long> = PublishSubject.create<Long>()
    val closeCommentsObservable: PublishSubject<Long> = PublishSubject.create<Long>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters[getItemViewType(position)]!!.bind(holder, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapters[viewType]!!.createVH(parent)
            .also { (it as? PostVH)?.clickListener = { (if (it.commentsOpened) closeCommentsObservable else openCommentsObservable).onNext(it.id) } }
            .also { (it as? LoadingCommentsVH)?.errorClickListener = { openCommentsObservable.onNext(it) } }

    override fun getItemViewType(position: Int) = items[position].viewType

    override fun getItemCount() = items.size
}