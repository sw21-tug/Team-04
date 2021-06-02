package com.example.traveltogether

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_group_chatting.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [group_chatting.newInstance] factory method to
 * create an instance of this fragment.
 */
class group_chatting : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val args: conversation_fragmentArgs by navArgs()
    lateinit var conversationRecyclerView: RecyclerView
    var messages:  MutableList<Message> = mutableListOf()

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
        val view = inflater.inflate(R.layout.fragment_group_chatting, container, false)
        conversationRecyclerView = view.findViewById(R.id.conversationRecyclerView)
        val activity = activity as Context
        val helperAdapter: ConversationHelperAdapter = ConversationHelperAdapter(activity, messages, this)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        conversationRecyclerView .layoutManager = linearLayoutManager
        conversationRecyclerView.adapter = helperAdapter
        val firebaseref = FirebaseDatabase.getInstance().reference

        Log.d("args_", args.chatId)
        firebaseref.child("posts").child(args.chatId).child("messages").addValueEventListener(object :
            ValueEventListener {
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment group_chatting.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            group_chatting().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}