package com.example.traveltogether

import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.*


class LoginUser {

    private val email:String
    private val name:String
    private val password:String
    var description:String

    constructor(email: String, name: String, password: String, description: String) {
        this.email = email
        this.name = name
        this.password = password
        this.description = description
    }

    fun signIn() {
        if(FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().signOut()
        }
        await(FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password))
    }

    fun createUser() {
        await(FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password))
        val updateProfile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        FirebaseAuth.getInstance().currentUser?.updateProfile(updateProfile)

    }

    fun deleteUser() {
        FirebaseAuth.getInstance().currentUser?.delete()
    }

}

