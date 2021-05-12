package com.example.traveltogether

import android.support.test.runner.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class AddComment {
    private lateinit var loginUser: LoginUser
    private lateinit var firebaseDb: FirebaseDatabase
    private lateinit var firebaseRef: DatabaseReference
    private var string = "test comment"

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun checkLogin () {
        loginUser = LoginUser("test@gmail.com", "Name", "12345678", "")
        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        val list : MutableList<String> = mutableListOf()
        firebaseRef.child("posts").push().
        setValue(UserPost(
            FirebaseAuth.getInstance().currentUser?.uid.toString(), "1",
            "Delete Test", "Malle", 1, 1,
            3, "hallo", list))
    }

    @Test
    fun comment() {
        var data = Tasks.await(firebaseRef.child("posts").get())
        var found = ""

        for (item in data.children) {
            assert(item.hasChild("title"))
            if (item.child("title").value.toString() == "Delete Test" &&
                item.child("uid").value.toString() ==
                FirebaseAuth.getInstance().currentUser?.uid
            ) {
                found = item.key.toString()
                break
            }
        }
        assert(found != "")

        val dataNew = Tasks.await(firebaseRef.child("posts").child(found).get())
        val list : MutableList<String> = mutableListOf()
        val user = UserPost(dataNew.child("uid").value.toString(),
            dataNew.key, dataNew.child("title").value.toString(),
            dataNew.child("destination").value.toString(),
            dataNew.child("startDate").value as Long,
            dataNew.child("endDate").value as Long,
            dataNew.child("numOfPeople").value as Long,
            dataNew.child("description").value.toString(), list)
        user.addComment(string)

        val data_ = Tasks.await(firebaseRef.child("posts").child(dataNew.key.toString()).get())
        assert(data_.child("comments").child("0").value.toString() == string)
    }
}