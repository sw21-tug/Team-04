package com.example.traveltogether

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click

import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditPostTest {

    @Test //how to test UI elements
    fun checkDisplay() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withId(R.id.edit_post_button)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_post_button)).perform(click())

        onView(withId(R.id.title_edit)).check(matches(isDisplayed()))
        onView(withId(R.id.starting_date_text)).check(matches(isDisplayed()))
        onView(withId(R.id.destination_text)).check(matches(isDisplayed()))
        onView(withId(R.id.number_people_text)).check(matches(isDisplayed()))
        onView(withId(R.id.duration_text)).check(matches(isDisplayed()))
        onView(withId(R.id.button3)).check(matches(isDisplayed()))

        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.edit_post_button)).check(matches(isDisplayed()))
    }

    @Test //how to test UI elements
    fun editPost() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withId(R.id.edit_post_button)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_post_button)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.title_edit)).check(matches(isDisplayed()))
        onView(withId(R.id.title_edit)).perform(ViewActions.clearText())
        onView(withId(R.id.title_edit)).perform(ViewActions.typeText("trip with bus"))
        pressBack()
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.edit_post_button)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_post_button)).perform(click())
        Thread.sleep(2000)
        onView(withText("trip with bus")).check(matches(isDisplayed()))

    }

}