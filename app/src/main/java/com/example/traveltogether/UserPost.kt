package com.example.traveltogether

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserPost (val UID: String, var PID: String?, var Title: String, var Destination: String, var StartDate: Long, var EndDate: Long,
                var NumOfPeople: Long?, var Description: String, var Comments: List<String>?){

    fun delete(): Boolean{
        if (UID != FirebaseAuth.getInstance().currentUser?.uid.toString())
            return false
        if(PID == null)
            return false
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference.child("posts").child(PID!!).ref
        firebaseref.removeValue()
        return true
    }
}