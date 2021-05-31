package com.example.traveltogether

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_post.view.*
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
        @SuppressLint("SetTextI18n", "ResourceType")
        fun bind(userPost: UserPost) {
            itemView.post_title.text = userPost.Title
            itemView.destination.text = userPost.Destination


            itemView.start_date.text =  context.getString(R.string.From)+ " " + getDate(userPost.StartDate)
            itemView.end_date.text = context.getString(R.string.to) + " "  + getDate(userPost.EndDate)
            itemView.description.text = userPost.Description
            itemView.num_people.text =   context.getString(R.string.Group_Size) + ": "   + userPost.NumOfPeople.toString()
            itemView.time_of_post.text = DateUtils.getRelativeTimeSpanString(userPost.TimePosted)

            itemView.comment_button.setOnClickListener {
                val actionArguments = all_post_fragmentDirections.actionAllPostFragmentToComment(
                    userPost.PID!!
                )
                view.findNavController().navigate(actionArguments)
            }
            itemView.join_group_chat.setOnClickListener {
                val actionArguments = all_post_fragmentDirections.actionAllPostFragmentToConversationFragment(userPost.PID!!)
                view.findNavController().navigate(actionArguments)

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