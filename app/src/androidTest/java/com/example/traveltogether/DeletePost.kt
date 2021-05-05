package com.example.traveltogether

import androidx.test.core.app.ActivityScenario
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
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
            Tasks.await(FirebaseAuth.getInstance().
                createUserWithEmailAndPassword("test@gmail.com", "12345678"))
        }
        val firebasereal = FirebaseDatabase.getInstance()
        val list : List<String> = emptyList()
        val firebaseref = firebasereal.reference.child("posts").push().
        setValue(UserPost(FirebaseAuth.getInstance().currentUser?.uid.toString(), "1",
                "Delete Test", "Malle", 1, 1,
                3, "hallo", list))
    }

    @Test
    fun DeletePost (){
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference

        var data = Tasks.await(firebaseref.child("posts").get())
        var found = ""

        for (item : DataSnapshot in data.children) {
            assert(item.hasChild("title"))
            if (item.child("title").value.toString() == "Delete Test" &&
                    item.child("uid").value.toString() ==
                    FirebaseAuth.getInstance().currentUser?.uid) {
                found = item.key.toString()
                break
            }
        }
        assert(!found.equals(""))

        val data_new = Tasks.await(firebaseref.child("posts").child(found).get())
        val user = UserPost(data_new.child("uid").value.toString(),
                data_new.key, data_new.child("title").value.toString(),
                data_new.child("destination").value.toString(),
                data_new.child("startDate").value as Long,
                data_new.child("endDate").value as Long,
                data_new.child("numOfPeople").value as Long,
                data_new.child("description").value.toString(), null)

        assert(data_new.key == found)
        user.delete()

        data = Tasks.await(firebaseref.child("posts").get())

        assert(!data.hasChild(found))

    }

}