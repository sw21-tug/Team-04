package com.example.traveltogether

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click

import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditPostTest {

    @Before
    fun setup () {
        if (FirebaseAuth.getInstance().currentUser == null)
            FirebaseAuth.getInstance().createUserWithEmailAndPassword("test@gmail.com", "12345678")
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withId(R.id.edit_post_button)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_post_button)).perform(click())
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
        onView(withId(R.id.edit_post_button)).check(matches(isDisplayed()))
    }

    @Test
    fun editPost() {
        Thread.sleep(2000)

        onView(withId(R.id.title_field)).check(matches(isDisplayed()))
        onView(withId(R.id.title_field)).perform(ViewActions.clearText())
        onView(withId(R.id.title_field)).perform(ViewActions.typeText("trip with bus"))
        pressBack()
        onView(withId(R.id.save_button)).perform(click())
        onView(withId(R.id.edit_post_button)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_post_button)).perform(click())
        Thread.sleep(2000)
        onView(withText("trip with bus")).check(matches(isDisplayed()))

    }

}