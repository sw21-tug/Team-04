package com.example.traveltogether

import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.*


class LoginUser(
    private val email: String,
    private val name: String,
    private val password: String,
    var description: String
) {

    fun signIn() {
        signOut()
        await(FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password))
    }

    fun signOut() {
        if(FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().signOut()
        }
    }

    fun createUser() {
        await(FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password))
        val updateProfile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        FirebaseAuth.getInstance().currentUser?.updateProfile(updateProfile)?.let { await(it) }

    }

    fun deleteUser() {
        FirebaseAuth.getInstance().currentUser?.delete()
    }

}

