package com.example.traveltogether

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter (val context : Context, val comments : List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        fun bind(s: Comment) {
            itemView.name_author_comment.text = s.uid
            itemView.time_posted_text_comment.text = DateUtils.getRelativeTimeSpanString(s.time)
            itemView.comment_text_view.text = s.comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount() = comments.size

}