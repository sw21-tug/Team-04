package com.example.traveltogether

import android.content.Context
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

class chat_fragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    var groupNames:  MutableList<Chat> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val firebaseref = FirebaseDatabase.getInstance().reference
        firebaseref.child("posts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupNames.clear()
                for ( post in snapshot.children) {
                    for(uid in post.child("userIDs").children)
                    {
                        if(uid.value == FirebaseAuth.getInstance().currentUser.uid)
                        {
                            val title = post.child("title").value.toString()
                            val pid = post.key.toString()
                            val chatItem = Chat(pid,title)
                            groupNames.add(chatItem)
                            break;
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })

        val view = inflater.inflate(R.layout.fragment_chat_fragment, container, false)
        recyclerView = view.findViewById(R.id.chatRecyclerView);
        val activity = activity as Context
        val helperAdapter = ChatHelperAdapter(activity, groupNames, this)
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView .layoutManager = linearLayoutManager
        recyclerView.adapter = helperAdapter
        return view
    }

}