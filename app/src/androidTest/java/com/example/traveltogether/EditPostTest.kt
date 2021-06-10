package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
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

@RunWith(AndroidJUnit4::class)
class EditPostTest {

    private lateinit var loginUser: LoginUser
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

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

    @Before
    fun setup () {
        loginUser = LoginUser("test@gmail.com", "Name","12345678", "")
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


        onView(withId(R.id.my_posts_fragment)).perform(click())
        onView(withId(R.id.Button_delete_my_posts)).check(matches(isDisplayed()))
        onView(withId(R.id.Button_delete_my_posts)).perform(click())
    }

    @After
    fun cleanup () {
        userPost.delete()
    }

    @Test
    fun checkDisplay() {

        onView(withId(R.id.title_field)).check(matches(isDisplayed()))
        onView(withId(R.id.starting_date_field)).check(matches(isDisplayed()))
        onView(withId(R.id.destination_field)).check(matches(isDisplayed()))
        onView(withId(R.id.number_people_field)).check(matches(isDisplayed()))
        onView(withId(R.id.ending_date_field)).check(matches(isDisplayed()))
        onView(withId(R.id.delete_button)).check(matches(isDisplayed()))
        onView(withId(R.id.save_button)).check(matches(isDisplayed()))


        onView(withId(R.id.save_button)).perform(click())
        onView(withId(R.id.Button_delete_my_posts)).check(matches(isDisplayed()))
    }

    @Test
    fun editPost() {
        Thread.sleep(2000)
        onView(withId(R.id.title_field)).check(matches(isDisplayed()))
        onView(withId(R.id.title_field)).perform(ViewActions.clearText())
        onView(withId(R.id.title_field)).perform(ViewActions.typeText("trip with bus"))
        pressBack()
        onView(withId(R.id.save_button)).perform(click())
        onView(withId(R.id.Button_delete_my_posts)).check(matches(isDisplayed()))
        onView(withId(R.id.Button_delete_my_posts)).perform(click())
        onView(withText("trip with bus")).check(matches(isDisplayed()))
    }

}