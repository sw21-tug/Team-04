package com.example.traveltogether

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class conversation_fragment : Fragment() {

    private val args: conversation_fragmentArgs by navArgs()
    lateinit var conversationRecyclerView: RecyclerView
    var messages: ArrayList<conversationMessage> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_conversation_fragment, container, false)
        conversationRecyclerView = view.findViewById(R.id.conversationRecyclerView)
        val activity = activity as Context
        val helperAdapter: ConversationHelperAdapter = ConversationHelperAdapter(activity, messages, this)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        conversationRecyclerView .layoutManager = linearLayoutManager
        conversationRecyclerView.adapter = helperAdapter
        return view
    }

}