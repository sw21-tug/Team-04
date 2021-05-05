package com.example.traveltogether

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class UserPost (val UID: String, val PID: Int, var Title: String, var Destination: String, var StartDate: Date?, var EndDate: Date?,
                var NumOfPeople: Int, var Description: String, var Comments: List<String>?) {

    fun delete(): Boolean{
        if (UID != FirebaseAuth.getInstance().currentUser?.uid.toString())
            return false
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("posts").child(PID.toString()).ref
        firebaseref.removeValue()
        return true
    }
}