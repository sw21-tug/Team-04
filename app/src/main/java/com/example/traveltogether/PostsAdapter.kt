package com.example.traveltogether

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_post.view.*
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_post.view.destination
import java.text.SimpleDateFormat
import java.util.*

class PostsAdapter (val context: Context, val posts: List<UserPost>) :
        RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userPost: UserPost) {
            itemView.post_title.text = userPost.Title
            itemView.destination.text = userPost.Destination
            itemView.start_date.text = getDateTime(userPost.StartDate)
            itemView.end_date.text = getDateTime(userPost.EndDate)
            itemView.description.text = userPost.Description
            itemView.num_people.text = userPost.NumOfPeople.toString()
            //look at video for e.g. some time ago
            //have to save time posted in userpost
        }
    }

    private fun getDateTime(l: Long): String? {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(l)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}