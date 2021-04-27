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
import com.google.firebase.auth.FirebaseAuth

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class DrawerMenuTest {

    @Test
    fun checkProfileActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.LEFT)))

        onView(withText("Profile")).perform(click())
        onView(withText("Profile")).check(matches(isDisplayed()))

    }

    @Test
    fun checkSettingsActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.LEFT)))

        onView(withText("Settings")).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))

    }

    @Test
    fun checkLogoutActivity() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.LEFT)))

        onView(withText("Logout")).perform(click())
        assertNull(FirebaseAuth.getInstance().currentUser)
       onView(withId(R.id.account_sign_in)).check(matches(isDisplayed()))

    }
}