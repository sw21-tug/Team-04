package com.example.traveltogether

import android.util.Log
import com.example.traveltogether.BuildConfig.DEBUG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.jar.Attributes
import com.google.firebase.database.ValueEventListener

class UserPost (val UID: String, val PID: String?, var TimePosted : Long, var Title: String, var Destination: String, var StartDate: Long, var EndDate: Long,
                var NumOfPeople: Long, var Description: String, var Comments: MutableList<Comment>?) {
class UserPost (val UID: String, val PID: String?, var Title: String, var Destination: String, var StartDate: Long, var EndDate: Long,
                var NumOfPeople: Long, var Description: String, var Comments: MutableList<Comment>?, var Messages: MutableList<Message>?, var UserID: MutableList<String>?) {


    fun post() {
        FirebaseDatabase.getInstance().reference.child("posts").push().setValue(this)
    }

    companion object {
        //not working properly
        fun getPost(pid: String): UserPost? {
            var firebase = FirebaseDatabase.getInstance()
            var firebaseReference = firebase.reference.child("posts").child(pid)
            var userPost: UserPost? = null
            var title : String = "title old"
            var timePosted : Long = 0
            var destination : String = "destination"
            var description : String = "description"
            var endDate : Long = 1
            var startDate : Long = 1
            var numOfPeople : Long = 1
            var uid : String = "uid"
            var pid : String = "pid"
            firebaseReference.child("title").setValue("new title")
            firebaseReference.child("title").addValueEventListener (object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    title = dataSnapshot.value.toString()
                }
                override fun onCancelled (databaseError: DatabaseError) { }
            })
            firebaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    title = dataSnapshot.child("title").value.toString()
                    destination = dataSnapshot.child("destination").value.toString()
                    description = dataSnapshot.child("description").value.toString()
                    //endDate = dataSnapshot.child("endDate").value.toString()
                    //startDate = dataSnapshot.child("startDate").value.toString()
                    //numOfPeople = dataSnapshot.child("numOfPeople").value.toString()
                    uid = dataSnapshot.child("uid").value.toString()
                    pid = dataSnapshot.child("pid").value.toString()
                    userPost = UserPost(uid, pid, timePosted, title, destination, startDate, endDate, numOfPeople, description, null)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            return userPost
        }
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