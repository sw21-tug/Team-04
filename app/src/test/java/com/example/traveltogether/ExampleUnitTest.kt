package com.example.traveltogether

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.*
import org.junit.Test
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.firebase.ui.auth.AuthUI


import org.junit.Assert.*
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun test_login_button () {
        onView(withId(R.id.account_sign_in)).perform(click()).check(matches(isEnabled()))

    }

    /*@Test //how to test UI elements
   fun greeterSaysHello() {
       onView(withId(R.id.name_field)).perform(typeText("Steve"))
       onView(withId(R.id.greet_button)).perform(click())
       onView(withText("Hello Steve!")).check(matches(isDisplayed()))
   }*/

    /*private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    @Test
    fun firebase() {
        //val database = FirebaseDatabase.getInstance()
        val myRef = firebaseDatabase.getReference("message")

        myRef.setValue("Hello, World!")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)!!
                Log.d(ContentValues.TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }*/
}