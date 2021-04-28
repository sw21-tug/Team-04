package com.example.traveltogether

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.rule.ActivityTestRule
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SettingTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SettingActivity::class.java)

    @Test
    fun changePasswordFalse () {
        onView(withId(R.id.change_pass)).perform(click())
        onView(withId(R.id.frameLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.TextPassword_Old)).perform(typeText("12345678"))
        onView(withId(R.id.TextPassword_new)).perform(typeText("1234"))
        onView(withId(R.id.TextPassword_new_again)).perform(typeText("1234"))
        onView(withId(R.id.submit_button)).perform(click())
    }

    @Test
    fun deleteUser () {
        onView(withId(R.id.delete_account)).perform(click())
        onView(withText("YES")).perform(click())
        assertNotNull(FirebaseAuth.getInstance().currentUser)
    }
}