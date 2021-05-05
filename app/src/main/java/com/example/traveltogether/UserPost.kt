package com.example.traveltogether

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserPost (val UID: String, val PID: Int?, var Title: String, var Destination: String, var StartDate: Long, var EndDate: Long,
                var NumOfPeople: Long, var Description: String, var Comments: List<String>?) {


    fun post() {
        FirebaseDatabase.getInstance().reference.child("posts").push().setValue(this)
    }

    fun delete(): Boolean {
        if (UID != FirebaseAuth.getInstance().currentUser?.uid.toString())
            return false
        if(PID == null)
            return false
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference.child("posts").child(PID.toString()).ref
        firebaseref.removeValue()
        return true
    }

    fun edit(UID: String, PID: Int){

    }
}