package com.example.traveltogether

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class ConversationHelperAdapter(var context: Context, var arrayList: List<Message>, var fragment: Fragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conversation_message, parent, false)
        return ViewHolderClass(view)
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolderClass: ViewHolderClass = holder as ViewHolderClass
        viewHolderClass.messageTextView.text = arrayList[position].message
        viewHolderClass.nameTextView.text = arrayList[position].name
        viewHolderClass.dateTextView.text = arrayList[position].time.toString()
    }

    override fun getItemCount(): Int{
        return arrayList.size
    }
    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var messageTextView: TextView
        var nameTextView: TextView
        var dateTextView: TextView

        init {
            messageTextView = itemView.findViewById(R.id.message_text_view)
            nameTextView = itemView.findViewById(R.id.name_text_view)
            dateTextView = itemView.findViewById(R.id.date_text_view)
        }
    }

}