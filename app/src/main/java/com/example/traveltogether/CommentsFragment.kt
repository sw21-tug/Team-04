package com.example.traveltogether

import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_comments.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Comment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var post: UserPost? = null
    private var param2: String? = null
    private var iterator: Int = 5
    private var five: Int = 5
    private val args : CommentsFragmentArgs by navArgs()

    private lateinit var comments : MutableList<Comment>
    private lateinit var adapter : CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

           // post = parameter
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_comments, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.comments_recycler_view)
        val pid = args.PID
        comments = mutableListOf()
        if (container != null) {
            adapter = CommentAdapter(container.context, comments)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(container.context)
            comments.clear()

            val firebaseref = FirebaseDatabase.getInstance().reference
            firebaseref.child("posts").child(pid).child("comments").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    comments.clear()
                    for (comment in snapshot.children.reversed()) {
                        if (comments.size >= 5)
                            break
                        val com = Comment(
                            comment.child("comment").value.toString(),
                            comment.child("uid").value.toString(),
                            comment.child("time").value as Long)
                        comments.add(com)
                    }
                    comments.reverse()
                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {}
            })

            val show_more_button = view.findViewById<Button>(R.id.show_more)
            show_more_button.setOnClickListener{
                val firebaseref = FirebaseDatabase.getInstance().reference
                firebaseref.child("posts").child(pid).child("comments").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        comments.clear()
                        for (comment in snapshot.children.reversed()) {
                            if (comments.size >= iterator + five)
                                break
                            val com = Comment(
                                comment.child("comment").value.toString(),
                                comment.child("uid").value.toString(),
                                comment.child("time").value as Long)

                            comments.add(com)
                            adapter.notifyDataSetChanged()
                        }
                        comments.reverse()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                iterator += 5
            }

            firebaseref.child("posts").child(pid).addValueEventListener(object  : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()) {
                        view.findViewById<TextView>(R.id.post_title).text = dataSnapshot.child("title").value.toString()
                        view.findViewById<TextView>(R.id.destination).text  = dataSnapshot.child("destination").value.toString()
                        view.findViewById<TextView>(R.id.description).text  = dataSnapshot.child("description").value.toString()
                        view.findViewById<TextView>(R.id.end_date).text  = "To " + getDate(dataSnapshot.child("endDate").value as Long)
                        view.findViewById<TextView>(R.id.start_date).text = "From " + getDate(dataSnapshot.child("startDate").value as Long)
                        view.findViewById<TextView>(R.id.num_people).text  = dataSnapshot.child("numOfPeople").value.toString()
                        view.findViewById<TextView>(R.id.time_of_post).text = DateUtils.getRelativeTimeSpanString(dataSnapshot.child("timePosted").value as Long)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            val send_button = view.findViewById<Button>(R.id.button_comment_send)
            send_button.setOnClickListener{
                val text = view.findViewById<TextView>(R.id.enter_comment_field).text.toString()
                if (text == "")
                    Toast.makeText(container.context, "Comment can't be empty!", Toast.LENGTH_LONG).show()
                else
                firebaseref.child("posts").child(pid).child("comments").push()
                    .setValue(Comment(text, if (FirebaseAuth.getInstance().currentUser?.displayName == "") "Anonymous"
                    else FirebaseAuth.getInstance().currentUser?.displayName , System.currentTimeMillis()))
                view.findViewById<TextView>(R.id.enter_comment_field).text = null
            }
        }



        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Comments.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: UserPost, param2: String) =
            CommentsFragment().apply {
                arguments = Bundle().apply {
                    post = param1
                    putString(ARG_PARAM2, param2)
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