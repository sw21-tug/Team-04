package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.traveltogether.R.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ProfileUnitTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileActivity::class.java)
    @Test
    fun checkEditButtonDisplay() {
        //onView(withId(id.edit_picture_button)).check(matches(isDisplayed()))
       // onView(withId(id.edit_picture_button)).check(matches(withText(string.profile_edit_picture)))
    }

    @Test
    fun checkEditButtonFunctionality() {
        //onView(withId(id.edit_picture_button)).perform(click())
    }

    @Test
    fun checkProfilePicture() {
        onView(withId(id.profile_picture)).check(matches(isDisplayed()))
    }

    @Test
    fun checkEditDescriptionButtonFunctionality() {
        onView(withId(id.save_description)).perform(click())
    }

    @Test
    fun checkEditDescription() {
        onView(withId(id.description_text)).check(matches(isDisplayed()))
    }

    @Test
    fun checkDescription() {
        onView(withId(id.save_description)).perform(click())
        onView(withId(id.et_editView)).perform(typeText("hallooo"))
    }
}