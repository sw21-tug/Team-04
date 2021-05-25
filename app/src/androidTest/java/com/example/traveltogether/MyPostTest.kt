package com.example.traveltogether

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class MyPostTest {
    private lateinit var loginUser: LoginUser
    private lateinit var firebaseDb: FirebaseDatabase
    private lateinit var firebaseRef: DatabaseReference
    private val postTitle = "Weit weg"
    private val postDestination = "Weg"
    private val postDescription = "...."
    private val postStartEndDate = 1621209800000L
    private val postNPeople = 3L
    private lateinit var pid : String
    private lateinit var userPost : UserPost
    private val myFormat = "dd/MM/yyyy"

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun checkLogin () {
        loginUser = LoginUser("mypoststests@gmail.com", "mypoststests", "mypoststests", "mypoststests")
        loginUser.signIn()
        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        val list : MutableList<Comment> = mutableListOf()
        firebaseRef.child("posts").push().
        setValue(UserPost(
            FirebaseAuth.getInstance().currentUser?.uid.toString(), "2", System.currentTimeMillis(),
                postTitle, postDestination, postStartEndDate, postStartEndDate,
                postNPeople, postDescription, list))
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
        var activityRule = ActivityScenarioRule(MainActivity::class.java)
        userPost.delete()
    }

    @Test
    fun checkDisplayContainer() {
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withId(R.id.recycler_view_my_post)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfPostIsDisplayed() {
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withText(postTitle)).check(matches(isDisplayed()))
        onView(withText(postDestination)).check(matches(isDisplayed()))
        onView(withText(postDescription)).check(matches(isDisplayed()))
        onView(withText("Group size: $postNPeople")).check(matches(isDisplayed()))

        val date = getDate(postStartEndDate)
        onView(withText("From $date")).check(matches(isDisplayed()))
        onView(withText("To $date")).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfPostEditButtonClickable() {
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withText("Edit Post"))
                .perform(click())

    }

    @Test
    fun checkIfPostCommentButtonClickable() {
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withText("Comments"))
                .perform(click())
    }

    @Test
    fun checkIfPostJoinButtonClickable() {
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withText("Join Group Chat"))
                .perform(click())
    }

    private fun getDate(l: Long): String? {
        return try {
            val sdf = SimpleDateFormat(myFormat)
            val netDate = Date(l)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}