package com.qwert2603.daily_fib

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_post.view.*


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)


class PostVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun TextView.plurals(res: Int, count: Int) {
        text = itemView.resources.getQuantityString(res, count, count)
        visibility = if (count > 0) View.VISIBLE else View.INVISIBLE
    }

    fun bind(post: Post) = with(itemView) {
        text_TextView.text = post.text
        likes_TextView.plurals(R.plurals.likes, post.likes.count)
        reposts_TextView.plurals(R.plurals.reposts, post.reposts.count)
        comments_TextView.plurals(R.plurals.comments, post.comments.count)
    }

}

class PostsAdapter : RecyclerView.Adapter<PostVH>() {

    var posts = listOf<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) = holder.bind(posts[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostVH(parent.inflate(R.layout.item_post))

    override fun getItemCount() = posts.size

    override fun getItemId(position: Int) = posts[position].id
}