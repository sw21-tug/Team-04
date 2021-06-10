package com.example.traveltogether

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [my_posts_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class my_posts_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var posts: MutableList<UserPost>
    private lateinit var adapter: MyPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_my_post_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_my_post)
        posts = mutableListOf()
        if (container != null) {
            adapter = MyPostAdapter(container.context, posts)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(container.context)
            posts.clear()
            val firebase = FirebaseDatabase.getInstance()
            val firebaseReference = firebase.reference.child("posts")

            firebaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    posts.clear()
                    for (snapshot in dataSnapshot.children) {
                        if (snapshot.child("uid").value.toString() == FirebaseAuth.getInstance().currentUser?.uid) {
                            val title = snapshot.child("title").value.toString()
                            val timePosted = snapshot.child("timePosted").value as Long
                            val destination = snapshot.child("destination").value.toString()
                            val description = snapshot.child("description").value.toString()
                            val endDate = snapshot.child("endDate").value as Long
                            val startDate = snapshot.child("startDate").value as Long
                            val numOfPeople = snapshot.child("numOfPeople").value as Long
                            val uid = snapshot.child("uid").value.toString()
                            val pid = snapshot.key.toString()

                            val userPost = UserPost(
                                uid,
                                pid,
                                timePosted,
                                title,
                                destination,
                                startDate,
                                endDate,
                                numOfPeople,
                                description,
                                null, null, null
                            )
                            posts.add(userPost)
                        }
                    }
                    posts.reverse()
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
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
         * @return A new instance of fragment profile_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            my_posts_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}