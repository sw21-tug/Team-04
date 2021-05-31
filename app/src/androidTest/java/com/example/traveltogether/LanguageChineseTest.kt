package com.example.traveltogether

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LanguageChineseTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SignIn::class.java)

    @Before
    fun setup(){
        val activityScenario = ActivityScenario.launch(SignIn::class.java)
        onView(withId(R.id.language_button)).perform(click())
        Thread.sleep(100)
        // add Language as in menu to choose (i.e. "Русские")
        onView(withText("中國人")).perform(click())
    }

    @After
    fun clean() {
        val activityScenario = ActivityScenario.launch(SignIn::class.java)
        onView(withId(R.id.language_button)).perform(click())
        Thread.sleep(100)
        // add Language as in menu to choose (i.e. "Русские")
        onView(withText("English")).perform(click())
    }

    @Test
    fun logIn () {
        val activityScenario = ActivityScenario.launch(SignIn::class.java)
        // @string sign_in_text
        onView(withText(R.string.sign_in_text)).check(matches(isDisplayed()))
        // already correct
        onView(withText("改變語言")).check(matches(isDisplayed()))
    }

    @Test
    fun mainActivity () {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        // @string all_text
        onView(withId(R.id.all_post_fragment)).check(matches(withContentDescription(R.string.all_text)))
        onView(withId(R.id.all_post_fragment)).perform(click())
        onView(withId(R.id.all_post_fragment)).check(matches(withContentDescription(R.string.all_text)))

        // @string saved_text
        onView(withId(R.id.saved_post_fragment)).check(matches(withContentDescription(R.string.saved_text)))
        onView(withId(R.id.saved_post_fragment)).perform(click())
        // @string saved_post_section_text
        onView(withText(R.string.saved_post_section_text)).check(matches(isDisplayed()))

        // @string chat_text
        onView(withId(R.id.chat_fragment)).check(matches(withContentDescription(R.string.chat_text)))
        onView(withId(R.id.chat_fragment)).perform(click())
        onView(withId(R.id.chat_fragment)).check(matches(withContentDescription(R.string.chat_text)))


        // @string new_text
        onView(withId(R.id.new_popup_fragment)).check(matches(withContentDescription(R.string.new_text)))
        onView(withId(R.id.new_popup_fragment)).perform(click())
        onView(withId(R.id.new_popup_fragment)).check(matches(withContentDescription(R.string.new_text)))
    }

    @Test
    fun menu () {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        // @string settings_text
        onView(withText(R.string.settings_text)).check(matches(isDisplayed()))
        //@string profile_name
        onView(withText(R.string.profile_name)).check(matches(isDisplayed()))
        //@string logout_text
        onView(withText(R.string.logout_text)).check(matches(isDisplayed()))
    }
    @Test
    fun profile () {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        // @string profile_text
        onView(withText(R.string.profile_text)).perform(click())
        onView(withText(R.string.profile_text)).check(matches(isDisplayed()))
        // @string profile_description
        onView(withText(R.string.profile_description)).check(matches(isDisplayed()))
        // @string profile_edit_description
        onView(withText(R.string.profile_edit_Description)).check(matches(isDisplayed()))
        // @string profile_edit_picture
        onView(withText(R.string.profile_edit_picture)).check(matches(isDisplayed()))
    }

    @Test
    fun chooseLanguage () {
        val activityScenario = ActivityScenario.launch(SignIn::class.java)
        onView(withId(R.id.language_button)).perform(click())
        Thread.sleep(100)
        // already correct
        onView(withText("Choose Language")).check(matches(isDisplayed()))
        // add Language as in menu to choose (i.e. "Русские")
        onView(withText("中國人")).check(matches(isDisplayed()))
        // already correct
        onView(withText("English")).check(matches(isDisplayed())).perform(click())
        onView(withText("Log In/Sign Up")).check(matches(isDisplayed()))
    }
}