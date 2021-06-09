package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SavedPostsTest {

    private lateinit var loginUser: LoginUser
    private lateinit var firebaseDb: FirebaseDatabase
    private lateinit var firebaseRef: DatabaseReference

    private var message_string = "test comment"
    private lateinit var pid : String
    private lateinit var userPost : UserPost
    private val post_name = "Saved Test"

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        FirebaseAuth.getInstance().signOut()
        loginUser = LoginUser("test@gmail.com", "Name", "12345678", "")
        loginUser.signIn()


        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        var list : MutableList<Comment> = mutableListOf()
        var messages : MutableList<Message> = mutableListOf()
        var ids : MutableList<String> = mutableListOf()
        firebaseRef.child("posts").push().
        setValue(UserPost(
            FirebaseAuth.getInstance().currentUser?.uid.toString(), "1", System.currentTimeMillis(),
            post_name, "Malle", 1, 1,
            3, "hallo", list, messages, ids))
        var data = Tasks.await(firebaseRef.child("posts").get())


        for (item in data.children) {
            assert(item.hasChild("title"))
            if (item.child("title").value.toString() == post_name &&
                item.child("uid").value.toString() ==
                FirebaseAuth.getInstance().currentUser?.uid
            ) {
                pid = item.key.toString()
                break
            }
        }
        assert(pid != "")

        firebaseRef.child("posts").child(pid).child("saved").push().setValue(FirebaseAuth.getInstance().currentUser.uid)
        val dataNew = Tasks.await(firebaseRef.child("posts").child(pid).get())

        userPost = UserPost(dataNew.child("uid").value.toString(),
            dataNew.key,dataNew.child("timePosted").value as Long,
            dataNew.child("title").value.toString(),
            dataNew.child("destination").value.toString(),
            dataNew.child("startDate").value as Long,
            dataNew.child("endDate").value as Long,
            dataNew.child("numOfPeople").value as Long,
            dataNew.child("description").value.toString(), list, messages, ids)



    }

    @After
    fun cleanup () {
        userPost.delete()
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun checkDisplaySaved () {
        onView(withId(R.id.saved_post_fragment)).perform(ViewActions.click())

        onView(ViewMatchers.withText(post_name)).check(ViewAssertions.matches((ViewMatchers.isDisplayed())))
    }

    @Test
    fun Unsave () {
        onView(withId(R.id.saved_post_fragment)).perform(ViewActions.click())
        Thread.sleep(1000)
        onView(withText(R.string.unsave)).perform(ViewActions.click())
        val data = Tasks.await(firebaseRef.child("posts").child(pid).get())
        assert(!data.hasChild("saved"))

    }



}