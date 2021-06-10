package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Rule
import java.time.LocalDateTime


private lateinit var firebaseDb: FirebaseDatabase
private lateinit var firebaseRef: DatabaseReference
private val postTitle = "hi"
private val postDestination = "graz"
private val postDescription = "trip"
private val postStartEndDate = System.currentTimeMillis()
private val postNPeople = 3L
private lateinit var pid : String
private lateinit var userPost : UserPost
private val myFormat = "dd/MM/yyyy"

@RunWith(AndroidJUnit4::class)
class FilterTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var loginUser: LoginUser

    @Before
    fun setup() {
        loginUser = LoginUser("markus123@gmail.com", "Markus", "markus123", "Hallo")
        loginUser.signIn()

        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        val list : MutableList<Comment> = mutableListOf()
        val messages : MutableList<Message> = mutableListOf()
        val ids : MutableList<String> = mutableListOf()
        firebaseRef.child("posts").push().
        setValue(UserPost(
                FirebaseAuth.getInstance().currentUser?.uid.toString(), "2", System.currentTimeMillis(),
                postTitle, postDestination, postStartEndDate, postStartEndDate,
                postNPeople, postDescription, list, messages, ids))
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
                dataNew.child("description").value.toString(), list, messages, ids)

        onView(withId(R.id.all_post_fragment)).perform(click())
    }

    @After
    fun cleanup () {
        userPost.delete()
    }


    @Test
    fun checkFilter(){

        onView(withId(R.id.filter_button_expand)).perform(click())
        onView(withId(R.id.filter_title)).perform(typeText("hi"))
        onView(withId(R.id.filter_destination)).perform(typeText("graz"))
        onView(withId(R.id.button_filter)).perform(click())
        onView(withId(R.id.filter_button_expand)).perform(click())
        Thread.sleep(2000)
        onView(withText("trip")).check(matches(isDisplayed()))
    }
}