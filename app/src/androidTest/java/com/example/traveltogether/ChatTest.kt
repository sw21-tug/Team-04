package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ChatTest {
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

        loginUser = LoginUser("markus123@gmail.com", "Markus", "markus123", "")
        loginUser.signIn()


        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        var list : MutableList<Comment> = mutableListOf()
        var messages : MutableList<Message> = mutableListOf()
        var ids : MutableList<String> = mutableListOf()
        firebaseRef.child("posts").push().
        setValue(UserPost(
            FirebaseAuth.getInstance().currentUser?.uid.toString(), "1",
            "Delete Test", "Malle", 1, 1,
            3, "hallo", list, messages, ids))
        var data = Tasks.await(firebaseRef.child("posts").get())


        for (item in data.children) {
            assert(item.hasChild("title"))
            if (item.child("title").value.toString() == "Delete Test" &&
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
            dataNew.key, dataNew.child("title").value.toString(),
            dataNew.child("destination").value.toString(),
            dataNew.child("startDate").value as Long,
            dataNew.child("endDate").value as Long,
            dataNew.child("numOfPeople").value as Long,
            dataNew.child("description").value.toString(), list, messages, ids)


        onView(withId(R.id.chat_fragment)).perform(click())
    }


    @Test
    fun chatUITest () {
        onView(withText("Hallo")).perform(click())
        onView(withId(R.id.sendMessageButton)).check(matches(isDisplayed()))
        onView(withId(R.id.messageEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.conversationRecyclerView)).check(matches(isDisplayed()))
    }


    //TODO call chat groug segment

    @Test
    fun functionalityAddingMessage()
    {
        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        firebaseRef.child("posts").child(pid).child("messages").push()
            .setValue(Message(message_string, FirebaseAuth.getInstance().currentUser.uid,  if (FirebaseAuth.getInstance().currentUser?.displayName == "") "Anonymous"
            else FirebaseAuth.getInstance().currentUser?.displayName.toString() , System.currentTimeMillis()))

        val data_ = Tasks.await(firebaseRef.child("posts").child(pid).child("messages").get())
        for (message in data_.children) {
            assert(message.child("message").value == message_string)
        }
    }


    @Test
    fun functionalityAddingUsersToChat()
    {
        firebaseDb = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDb.reference
        firebaseRef.child("posts").child(pid).child("userIDs").push()
            .setValue(FirebaseAuth.getInstance().currentUser.uid)

        val data_ = Tasks.await(firebaseRef.child("posts").child(pid).child("userIDs").get())
        for (id in data_.children) {
            assert(id.value == message_string)
        }
    }

    @Test
    fun checkDisplayOfMessage() {
        onView(withId(R.id.conversation_fragment)).perform(click())
        onView(withId(R.id.messageEditText)).perform(ViewActions.typeText(message_string))
        onView(withId(R.id.sendMessageButton)).perform(click())


        onView(withText(message_string)).check(matches(isDisplayed()))

    }



}