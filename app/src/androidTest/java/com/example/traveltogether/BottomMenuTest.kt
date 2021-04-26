package com.example.traveltogether

import android.content.Intent
import android.view.Gravity
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class BottomMenuTest {

    @Test //how to test UI elements
    fun checksavedpostsfragment() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withText("Saved Post Section")).check(matches(isDisplayed()))
    }
    @Test //how to test UI elements
    fun checkallpostfragment() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.all_post_fragment)).perform(click())
        onView(withText("All Posts")).check(matches(isDisplayed()))
    }
    @Test //how to test UI elements
    fun checkpopupfragment() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.new_popup_fragment)).perform(click())
        onView(withText("Newest Popup")).check(matches(isDisplayed()))
    }
    @Test //how to test UI elements
    fun checkchatpostfragment() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.chat_fragment)).perform(click())
        onView(withText("Chat Section")).check(matches(isDisplayed()))
    }
    @Test //how to test UI elements
    fun checknewsfragment() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.news_fragment)).perform(click())
        onView(withText("News Section")).check(matches(isDisplayed()))
    }

}