package com.example.traveltogether

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_group_chatting.*

class conversation_fragment : Fragment() {
    private val args: conversation_fragmentArgs by navArgs()


    private lateinit var conversationFragmentAdapter: ConversationFragmentAdapter
    private lateinit var viewPager: ViewPager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val chat_title = getString(R.string.group_chat_messages_title)
        val planning_title = getString(R.string.group_chat_planing_title)
        getString(R.string.app_name)
        conversationFragmentAdapter = ConversationFragmentAdapter(childFragmentManager, chat_title, planning_title, args.chatId)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = conversationFragmentAdapter
    }

}
// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class ConversationFragmentAdapter(
    fm: FragmentManager,
    private val frag1Name: String,
    private val frag2Name: String,
    private val chatId: String
) : FragmentStatePagerAdapter(fm) {



    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        return ConversationFragment(i, chatId)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if(position == 0) {
            frag1Name
        } else {
            frag2Name
        }
    }
}

class ConversationFragment(var frag_number: Int, var chatId: String) : Fragment() {
    lateinit var conversationRecyclerView: RecyclerView
    var messages:  MutableList<Message> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(frag_number == 0) {
            return getGroupChattingView(inflater, container)
        } else {
            return inflater.inflate(R.layout.fragment_group_planning, container, false)
        }
    }

    private fun getGroupChattingView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        //inflater.inflate(R.layout.fragment_group_chatting, container, false)
        val view = inflater.inflate(R.layout.fragment_group_chatting, container, false)
        conversationRecyclerView = view.findViewById(R.id.conversationRecyclerView)
        val activity = activity as Context
        val helperAdapter: ConversationHelperAdapter =
            ConversationHelperAdapter(activity, messages, this)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        conversationRecyclerView.layoutManager = linearLayoutManager
        conversationRecyclerView.adapter = helperAdapter
        val firebaseref = FirebaseDatabase.getInstance().reference

        Log.d("args_", chatId)
        firebaseref.child("posts").child(chatId).child("messages").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (msg in snapshot.children) {
                    val name = msg.child("name").value.toString()
                    val uid = msg.child("uid").value.toString()
                    val time = msg.child("time").value as Long
                    val text = msg.child("message").value.toString()
                    val msgItem = Message(text, uid, name, time)
                    messages.add(msgItem)
                }
                helperAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        val sendMessageButton = view.findViewById<FloatingActionButton>(R.id.sendMessageButton)
        sendMessageButton.setOnClickListener {
            val msg = Message(
                messageEditText.text.toString(),
                FirebaseAuth.getInstance().currentUser.uid,
                if (FirebaseAuth.getInstance().currentUser?.displayName == "") "Anonymous"
                else FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                System.currentTimeMillis()
            )
            messageEditText.text = null
            firebaseref.child("posts").child(chatId).child("messages").push().setValue(msg)
        }
        return view
    }
}

