package com.example.traveltogether

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.data.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_conversation_fragment.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class conversation_fragment : Fragment() {

    private val args: conversation_fragmentArgs by navArgs()
    lateinit var conversationRecyclerView: RecyclerView
    var messages:  MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_conversation_fragment, container, false)
        val leaveGroupChatButton = view.findViewById<Button>(R.id.leave_group_chat)
        var firebaseref = FirebaseDatabase.getInstance().reference
        leaveGroupChatButton.setOnClickListener{
            firebaseref.child("posts").child(args.chatId).child("uid").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (uid in snapshot.children) {
                        if (uid.value == FirebaseAuth.getInstance().currentUser.uid) {
                            firebaseref.child("posts").child(args.chatId).child("uid").child(uid.key.toString()).removeValue()
                            break
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            findNavController().navigate(R.id.action_conversation_fragment_to_chat_fragment)

        }


        conversationRecyclerView = view.findViewById(R.id.conversationRecyclerView)
        val activity = activity as Context
        val helperAdapter: ConversationHelperAdapter = ConversationHelperAdapter(activity, messages, this)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        conversationRecyclerView .layoutManager = linearLayoutManager
        conversationRecyclerView.adapter = helperAdapter
        firebaseref = FirebaseDatabase.getInstance().reference
        firebaseref.child("posts").child(args.chatId).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for ( msg in snapshot.children) {
                    val name = msg.child("name").value.toString()
                    val uid = msg.child("uid").value.toString()
                    val time = msg.child("time").value as Long
                    val text = msg.child("message").value.toString()
                    val msgItem = Message(text,uid,name,time)
                    messages.add(msgItem)
                }
                helperAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        val sendMessageButton = view.findViewById<FloatingActionButton>(R.id.sendMessageButton)
        sendMessageButton.setOnClickListener{
            val msg = Message(messageEditText.text.toString(), FirebaseAuth.getInstance().currentUser.uid,  if (FirebaseAuth.getInstance().currentUser?.displayName == "") "Anonymous"
            else FirebaseAuth.getInstance().currentUser?.displayName.toString() ,System.currentTimeMillis())
            messageEditText.text = null
            firebaseref.child("posts").child(args.chatId).child("messages").push().setValue(msg)
        }
        return view
    }



}