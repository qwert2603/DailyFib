package com.qwert2603.daily_fib.main_activity.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import com.qwert2603.daily_fib.R
import com.qwert2603.daily_fib.main_activity.CommentItem
import com.qwert2603.daily_fib.util.inflate
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentDelegateAdapter : DelegateAdapter<CommentItem, CommentVH> {
    override fun createVH(parent: ViewGroup): CommentVH {
        return CommentVH(parent.inflate(R.layout.item_comment))
    }

    override fun bind(viewHolder: CommentVH, item: CommentItem) {
        viewHolder.bind(item)
    }
}

class CommentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(commentItem: CommentItem) = with(itemView) {
        val stb = SpannableStringBuilder("${commentItem.author.name} ${commentItem.text}")
        stb.setSpan(StyleSpan(Typeface.BOLD), 0, commentItem.author.name.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        textComment_TextView.text = stb
        ImageLoader.getInstance().displayImage(commentItem.author.photo, photo_ImageView)
    }
}