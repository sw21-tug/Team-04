package com.example.traveltogether

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class chat_fragment : Fragment() {
    lateinit var recyclerView: RecyclerView;
    var name: ArrayList<String> = ArrayList();
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView);
        name.add("Markus is faul!!")
        name.add("Freddy stinkt!!")
        Log.d("Fuck it",name[0])
        val activity = activity as Context
        val helperAdapter: ChatHelperAdapter = ChatHelperAdapter(activity,name)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        recyclerView .layoutManager = linearLayoutManager
        recyclerView.adapter = helperAdapter
        return view
    }

}