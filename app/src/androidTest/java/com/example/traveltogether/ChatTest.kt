package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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

    @Before
    fun setup() {

        loginUser = LoginUser("markus123@gmail.com", "Markus", "markus123", "")
        loginUser.signIn()
        onView(withId(R.id.chat_fragment)).perform(click())
    }


    @Test
    fun chatUITest () {
        onView(withText("Hallo")).perform(click())
        onView(withId(R.id.sendMessageButton)).check(matches(isDisplayed()))
        onView(withId(R.id.messageEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.conversationRecyclerView)).check(matches(isDisplayed()))
    }

}