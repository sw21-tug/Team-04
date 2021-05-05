package com.example.traveltogether

import android.support.test.runner.AndroidJUnit4
import androidx.test.core.app.ActivityScenario
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class DeletePost {
    @Before
    fun checkLogin () {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        if (FirebaseAuth.getInstance().currentUser == null) {
            Tasks.await(FirebaseAuth.getInstance().signInWithEmailAndPassword("armin@gmail.com", "12345678"))
        }
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference.child("posts").child("1").
                        setValue(UserPost(FirebaseAuth.getInstance().currentUser?.uid.toString(),
                            1, "Delete Test", "Malle", null, null,
                            3, "hallo", null))
    }

    @Test
    fun DeletePost (){
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference

        var data = Tasks.await(firebaseref.child("posts").get())
        assert(data.hasChild("1"))

        firebaseref.child("posts").child("1").removeValue()
        data = Tasks.await(firebaseref.child("posts").get())
        assert(!data.hasChild("1"))

    }

}