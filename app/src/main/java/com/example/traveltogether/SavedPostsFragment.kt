package com.example.traveltogether

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_all_post_fragment.*
import kotlinx.android.synthetic.main.item_post.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [saved_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class saved_post_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var posts: MutableList<UserPost>
    private lateinit var tempPost: MutableList<UserPost>
    private lateinit var adapter : SavedPostsAdapter
    private lateinit var recyclerView : RecyclerView
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
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_saved_post_fragment, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_saved_posts)
        posts = mutableListOf()
        tempPost = mutableListOf()
        if (container != null) {
            adapter = SavedPostsAdapter(container.context, tempPost)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(container.context)
            posts.clear()

            var firebase = FirebaseDatabase.getInstance()
            var firebaseReference = firebase.reference.child("posts")

            firebaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    posts.clear()
                    tempPost.clear()
                    for (snapshot in dataSnapshot.children) {
                        for(saved in snapshot.child("saved").children)
                        {
                            if(saved.value == FirebaseAuth.getInstance().currentUser.uid)
                            {
                                val title = snapshot.child("title").value.toString()
                                val timePosted = snapshot.child("timePosted").value as Long
                                val destination = snapshot.child("destination").value.toString()
                                val description = snapshot.child("description").value.toString()
                                val endDate = snapshot.child("endDate").value as Long
                                val startDate = snapshot.child("startDate").value as Long
                                val numOfPeople = snapshot.child("numOfPeople").value as Long
                                val uid = snapshot.child("uid").value.toString()
                                val pid = snapshot.key.toString()

                                val userPost = UserPost(uid, pid, timePosted, title, destination, startDate, endDate, numOfPeople, description, null, null, null)
                                posts.add(userPost)
                                break
                            }
                        }
                    }
                    tempPost.addAll(posts)
                    posts.reverse()
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            var filter_button = view.findViewById<View>(R.id.filter_button) as Button
            var text = view.findViewById<View>(R.id.search_text) as EditText

            filter_button.setOnClickListener {
                tempPost.clear()
                val searchtext = text.text.toString()
                if (searchtext.isNotEmpty())
                {
                    // TODO Datenbank abgleichen
                    posts.forEach{
                        if (it.Title.contains(searchtext)){
                            tempPost.add(it)
                        }
                        else if(it.Description.contains(searchtext)) {
                            tempPost.add(it)
                        }
                        else if(it.Destination.contains(searchtext)) {
                            tempPost.add(it)
                        }
                        else if(it.UID.contains(searchtext)) {
                            tempPost.add(it)
                        }

                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                else{
                    tempPost.clear()
                    tempPost.addAll(posts)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment settings_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            all_post_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}