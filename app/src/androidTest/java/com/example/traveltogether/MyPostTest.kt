package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class MyPostTest {
    private lateinit var loginUser: LoginUser
    private lateinit var firebaseDb: FirebaseDatabase
    private lateinit var firebaseRef: DatabaseReference
    private val string = "test comment"
    private val postTitle = "Urlaub"
    private lateinit var pid : String
    private lateinit var userPost : UserPost

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun checkLogin () {
        loginUser = LoginUser("test@gmail.com", "Name", "12345678", "")
        loginUser.signIn()
        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        val list : MutableList<Comment> = mutableListOf()
        firebaseRef.child("posts").push().
        setValue(UserPost(
            FirebaseAuth.getInstance().currentUser?.uid.toString(), "1", System.currentTimeMillis(),
                postTitle, "Malle", 1, 1,
            3, "hallo", list))
        val data = Tasks.await(firebaseRef.child("posts").get())


        for (item in data.children) {
            assert(item.hasChild("title"))
            if (item.child("title").value.toString() == postTitle &&
                item.child("uid").value.toString() ==
                FirebaseAuth.getInstance().currentUser?.uid
            ) {
                pid = item.key.toString()
                break
            }
        }
        assert(pid != "")

        val dataNew = Tasks.await(firebaseRef.child("posts").child(pid).get())

        userPost = UserPost(dataNew.child("uid").value.toString(),
            dataNew.key, dataNew.child("timePosted").value as Long, dataNew.child("title").value.toString(),
            dataNew.child("destination").value.toString(),
            dataNew.child("startDate").value as Long,
            dataNew.child("endDate").value as Long,
            dataNew.child("numOfPeople").value as Long,
            dataNew.child("description").value.toString(), list)
    }

    @After
    fun cleanup () {
        userPost.delete()
    }

    @Test
    fun checkDisplay() {
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withId(R.id.recycler_view_my_post)).check(matches(isDisplayed()))
    }

}