package com.example.traveltogether

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_my_post.view.*
import java.text.SimpleDateFormat
import java.util.*


class MyPostAdapter(val context: Context, val posts: List<UserPost>) :
    RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {
    private lateinit var view : View
    private lateinit var buttonComments : Button

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.item_my_post, parent, false)
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
                val actionArguments = my_posts_fragmentDirections.actionMyPostsFragmentToComment(
                    userPost.PID!!
                )
                view.findNavController().navigate(actionArguments)
            }
            itemView.join_group_chat.setOnClickListener {
                val firebaseref = FirebaseDatabase.getInstance().reference
                firebaseref.child("posts").child(userPost.PID.toString()).child("uid").push().setValue(FirebaseAuth.getInstance().currentUser.uid)

                val actionArguments = my_posts_fragmentDirections.actionMyPostsFragmentToConversationFragment(userPost.PID!!)
                view.findNavController().navigate(actionArguments)
            }
            itemView.Button_delete_my_posts.setOnClickListener {
                val actionArguments = my_posts_fragmentDirections.actionMyPostsFragmentToPostEdit(userPost.PID!!)
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