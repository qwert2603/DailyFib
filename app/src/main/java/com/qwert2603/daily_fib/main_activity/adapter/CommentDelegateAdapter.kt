package com.qwert2603.daily_fib.main_activity.adapter

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
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
import com.qwert2603.daily_fib.util.setVisible
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentDelegateAdapter : DelegateAdapter<CommentItem, CommentVH> {
    override fun createVH(parent: ViewGroup) = CommentVH(parent.inflate(R.layout.item_comment))

    override fun bind(viewHolder: CommentVH, item: CommentItem) {
        viewHolder.bind(item)
    }
}

class CommentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.photos_RecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            itemAnimator = null
        }
    }

    fun bind(commentItem: CommentItem) = with(itemView) {
        // todo: commentItem.date
        val stb = SpannableStringBuilder("${commentItem.author.name} ${commentItem.text}")
        stb.setSpan(StyleSpan(Typeface.BOLD), 0, commentItem.author.name.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        textComment_TextView.text = stb
        ImageLoader.getInstance().displayImage(commentItem.author.photo, photo_ImageView)
        if (commentItem.photos.isNotEmpty()) {
            photos_RecyclerView.setVisible(true)
            photos_RecyclerView.adapter = PhotosAdapter(commentItem.photos)
        } else {
            photos_RecyclerView.setVisible(false)
        }
    }

    class PhotosAdapter(private val photos: List<String>) : RecyclerView.Adapter<PhotoVH>() {
        override fun onBindViewHolder(holder: PhotoVH, position: Int) {
            holder.bind(photos[position])
        }

        override fun getItemCount() = photos.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoVH(parent.inflate(R.layout.item_photo))
    }

    class PhotoVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(url: String) {
            ImageLoader.getInstance().displayImage(url, itemView.photo_ImageView)
        }
    }
}