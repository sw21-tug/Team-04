package com.example.traveltogether

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
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
class JoinChatTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var loginUser: LoginUser
    private lateinit var firebaseDb: FirebaseDatabase
    private lateinit var firebaseRef: DatabaseReference

    private var message_string = "test comment"
    private lateinit var pid : String
    private lateinit var userPost : UserPost
    @Before
    fun setup() {
        FirebaseAuth.getInstance().signOut()
        loginUser = LoginUser("markus123@gmail.com", "Markus", "markus123", "")
        loginUser.signIn()


        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        val list : MutableList<Comment> = mutableListOf()
        val messages : MutableList<Message> = mutableListOf()
        val ids : MutableList<String> = mutableListOf()
        firebaseRef.child("posts").push().
        setValue(UserPost(
                FirebaseAuth.getInstance().currentUser?.uid.toString(), "1", System.currentTimeMillis(),
                "post", "Malle", 1, 1,
                3, "hallo", list, messages, ids))
        var data = Tasks.await(firebaseRef.child("posts").get())


        for (item in data.children) {
            assert(item.hasChild("title"))
            if (item.child("title").value.toString() == "post" &&
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
                dataNew.key,dataNew.child("timePosted").value as Long,
                dataNew.child("title").value.toString(),
                dataNew.child("destination").value.toString(),
                dataNew.child("startDate").value as Long,
                dataNew.child("endDate").value as Long,
                dataNew.child("numOfPeople").value as Long,
                dataNew.child("description").value.toString(), list, messages, ids)


        Espresso.onView(ViewMatchers.withId(R.id.chat_fragment)).perform(ViewActions.click())
    }

    @After
    fun cleanup () {
        userPost.delete()
    }

    @Test
    fun joinChatTest () {

        Espresso.onView(ViewMatchers.withId(R.id.my_posts_fragment)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.join_group_chat)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.chat_fragment)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("post")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("post")).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.conversationRecyclerView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

}