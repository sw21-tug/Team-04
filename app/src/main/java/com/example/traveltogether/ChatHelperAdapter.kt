package com.example.traveltogether

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView


class ChatHelperAdapter(var context: Context,var list: List<Chat>, var fragment: Fragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        val viewHolderClass = ViewHolderClass(view)
        return viewHolderClass;
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolderClass: ViewHolderClass = holder as ViewHolderClass
        viewHolderClass.textView.text = list[position].title
        viewHolderClass.itemView.setOnClickListener {
            val args = chat_fragmentDirections.actionChatFragmentToConversationFragment(list[position].PID)
            fragment.findNavController().navigate(args)
        }
    }

    override fun getItemCount(): Int{
        return list.size;
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.chat_item_text_view)
    }

}