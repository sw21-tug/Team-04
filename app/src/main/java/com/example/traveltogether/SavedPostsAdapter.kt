package com.example.traveltogether

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_post.view.comment_button
import kotlinx.android.synthetic.main.item_post.view.description
import kotlinx.android.synthetic.main.item_post.view.destination
import kotlinx.android.synthetic.main.item_post.view.end_date
import kotlinx.android.synthetic.main.item_post.view.join_group_chat
import kotlinx.android.synthetic.main.item_post.view.num_people
import kotlinx.android.synthetic.main.item_post.view.post_title
import kotlinx.android.synthetic.main.item_post.view.start_date
import kotlinx.android.synthetic.main.item_post.view.time_of_post
import kotlinx.android.synthetic.main.item_save_post.view.*
import java.text.SimpleDateFormat
import java.util.*


class SavedPostsAdapter(val context: Context, val posts: List<UserPost>) :
        RecyclerView.Adapter<SavedPostsAdapter.ViewHolder>() {
    private lateinit var view : View
    private lateinit var buttonComments : Button

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.item_save_post, parent, false)
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
                val actionArguments = saved_post_fragmentDirections.actionSavedPostFragmentToComment(
                    userPost.PID!!
                )
                view.findNavController().navigate(actionArguments)
            }
            itemView.join_group_chat.setOnClickListener {
                //TODO add do chatfragment in database
                val firebaseref = FirebaseDatabase.getInstance().reference
                firebaseref.child("posts").child(userPost.PID.toString()).child("uid").push().setValue(FirebaseAuth.getInstance().currentUser.uid)

                val actionArguments = saved_post_fragmentDirections.actionSavedPostFragmentToConversationFragment(userPost.PID!!)
                view.findNavController().navigate(actionArguments)

            }
            itemView.unsave_post_button.setOnClickListener{
                var firebase = FirebaseDatabase.getInstance()
                var firebaseReference = firebase.reference.child("posts")

                firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            if(snapshot.key.toString() == userPost.PID)
                            {
                                for (saved in snapshot.child("saved").children)
                                {
                                    if (saved.value == FirebaseAuth.getInstance().currentUser.uid) {
                                        firebaseReference.child(userPost.PID.toString())
                                            .child("saved").child(saved.key.toString())
                                            .removeValue()
                                    }
                                }
                                break
                            }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
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