package com.qwert2603.daily_fib.main_activity.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qwert2603.daily_fib.R
import com.qwert2603.daily_fib.main_activity.PostItem
import com.qwert2603.daily_fib.util.inflate
import kotlinx.android.synthetic.main.item_post.view.*

class PostDelegateAdapter : DelegateAdapter<PostItem, PostVH> {
    override fun createVH(parent: ViewGroup): PostVH {
        return PostVH(parent.inflate(R.layout.item_post))
    }

    override fun bind(viewHolder: PostVH, item: PostItem) {
        viewHolder.bind(item)
    }
}

class PostVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var clickListener: ((PostItem) -> Unit)? = null
    lateinit var postItem: PostItem

    init {
        itemView.setOnClickListener { clickListener?.invoke(postItem) }
    }

    @SuppressLint("SetTextI18n")
    fun TextView.plurals(count: Int, letter: Char) {
        text = "$count $letter"
        visibility = if (count > 0) View.VISIBLE else View.INVISIBLE
    }

    fun bind(postItem: PostItem) = with(itemView) {
        // todo: postItem.date
        this@PostVH.postItem = postItem
        text_TextView.text = postItem.text
        number_TextView.text = "#${postItem.number}"
        likes_TextView.plurals(postItem.likes, 'L')
        reposts_TextView.plurals(postItem.reposts, 'R')
        views_TextView.plurals(postItem.views, 'V')
        comments_TextView.plurals(postItem.comments, 'C')
    }
}