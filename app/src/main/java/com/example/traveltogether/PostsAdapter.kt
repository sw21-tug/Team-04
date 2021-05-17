package com.example.traveltogether

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_post.view.destination
import java.text.SimpleDateFormat
import java.util.*


class PostsAdapter(val context: Context, val posts: List<UserPost>) :
        RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    private lateinit var view : View
    private lateinit var buttonComments : Button

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
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
            itemView.start_date.text = "From " + getDate(userPost.StartDate)
            itemView.end_date.text = "To " + getDate(userPost.EndDate)
            itemView.description.text = userPost.Description
            itemView.num_people.text = "Group size: " + userPost.NumOfPeople.toString()
            itemView.time_of_post.text = DateUtils.getRelativeTimeSpanString(userPost.TimePosted)

            itemView.comment_button.setOnClickListener {
                itemView.post_title.text = "comment button worked" //add go to fragment
            }
            itemView.join_group_chat.setOnClickListener {
                itemView.post_title.text = "join group chat button worked" //add go to fragment
            }
        }
    }

    private fun getDate(l: Long): String? {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(l)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }


}