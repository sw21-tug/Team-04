package com.example.traveltogether

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.jar.Attributes

class UserPost (val UID: String, val PID: String?, var Title: String, var Destination: String, var StartDate: Long, var EndDate: Long,
                var NumOfPeople: Long, var Description: String, var Comments: MutableList<Comment>?, var Messages: MutableList<Message>?, var UserID: MutableList<String>?) {


    fun post() {
        FirebaseDatabase.getInstance().reference.child("posts").push().setValue(this)
    }

    fun delete(): Boolean {
        if (UID != FirebaseAuth.getInstance().currentUser?.uid.toString())
            return false
        if(PID == null)
            return false
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference.child("posts").child(PID).ref
        firebaseref.removeValue()
        return true
    }

    fun edit(UID: String, PID: Int){
    }

    fun addComment (comment : String) {
        val firebase = FirebaseDatabase.getInstance().reference
        val com = Comment(comment, UID, System.currentTimeMillis())
        Comments?.add(com)
        firebase.child("posts").child(PID.toString()).child("comments").push().setValue(com)
    }
    fun addMessage (message : String){
        val firebase = FirebaseDatabase.getInstance().reference
        val msg = Message(message, UID, FirebaseAuth.getInstance().currentUser?.displayName.toString() ,System.currentTimeMillis())
        Messages?.add(msg)
        firebase.child("posts").child(PID.toString()).child("messages").push().setValue(msg)
    }
    //todo add to clicklistener in postadpater
    fun addPostChat(PID: String){
        val firebase = FirebaseDatabase.getInstance().reference
        firebase.child("posts").child(PID).child("userIDs").child(FirebaseAuth.getInstance().currentUser.uid)
    }
}