package com.example.traveltogether

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*



class UserPost (val UID: String, var Title: String, var Destination: String, var StartDate: Long, var EndDate: Long,
                var NumOfPeople: Long, var Description: String, var Comments: List<String>?) {


    fun post() {
        FirebaseDatabase.getInstance().reference.child("posts").push().setValue(this)
    }

    fun delete(UID: String, PID: Int){

    }
    fun edit(UID: String, PID: Int){

    }
}