package com.example.traveltogether

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.*
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
class SignInChangePasswordTest {

    private lateinit var email:String
    private lateinit var name:String
    private lateinit var password:String
    private lateinit var description:String
    private lateinit var loginUser: LoginUser

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup(){
        email = getRandomString(10)
        name = getRandomString(10)
        password = getRandomString(10)
        description = getRandomString(10)

        loginUser = LoginUser("$email@gmail.com", name, password, description)
        loginUser.createUser()
    }

    @Test
    fun loginAndChangePassword () {
        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isOpen(Gravity.LEFT)))
        onView(withText("Settings")).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))

        onView(withId(R.id.change_pass)).perform(click())
        onView(withId(R.id.frameLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.TextPassword_Old)).perform(typeText(password))
        onView(withId(R.id.TextPassword_new)).perform(typeText("password"))
        onView(withId(R.id.TextPassword_new_again)).perform(typeText("password"))
        onView(withId(R.id.submit_button)).perform(click())
    }

    private fun getRandomString(size: Int ) : String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..size)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}