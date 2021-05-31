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
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class BottomMenuTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkSavedPostsFragment() {
        onView(withId(R.id.saved_post_fragment)).perform(click())
        onView(withText(R.string.save_title_text)).check(matches(isDisplayed()))
    }
    @Test
    fun checkAllPostFragment() {
        onView(withId(R.id.all_post_fragment)).perform(click())
        onView(withId(R.id.all_post_fragment)).check(matches(isDisplayed()))
    }
    @Test
    fun checkPopupFragment() {
        onView(withId(R.id.new_popup_fragment)).perform(click())
        onView(withId(R.id.new_popup_fragment)).check(matches(isDisplayed()))
    }
    @Test
    fun checkChatPostFragment() {
        onView(withId(R.id.chat_fragment)).perform(click())
        onView(withId(R.id.chat_fragment)).check(matches(isDisplayed()))
    }
}