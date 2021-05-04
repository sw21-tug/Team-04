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

    @get:Rule
    val activityRule = ActivityTestRule(SignIn::class.java)

    @Before
    fun setup(){
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun loginAndChangePasswort () {
        assertNull(FirebaseAuth.getInstance().currentUser)
        val loginSignUpButton = withId(R.id.account_sign_in)

        onView(loginSignUpButton).perform(click())

        val signInProviders = listOf(AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true)
            .setRequireName(true)
            .build())

        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(signInProviders)
            .build()

        assertEquals(intent.data, activityRule.activity.intent.data)

        onView(withId(R.id.email)).perform(typeText("manar1@gmail.com"))

        onView(withId(R.id.button_next)).perform(click())

        onView(withId(R.id.password)).perform(typeText("manar123"))
        onView(withId(R.id.button_done)).perform(click())

        assertNotNull(FirebaseAuth.getInstance().currentUser)

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isOpen(Gravity.LEFT)))
        onView(withText("Settings")).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))

        onView(withId(R.id.change_pass)).perform(click())
        onView(withId(R.id.frameLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.TextPassword_Old)).perform(typeText("manar123"))
        onView(withId(R.id.TextPassword_new)).perform(typeText("Markus1234"))
        onView(withId(R.id.TextPassword_new_again)).perform(typeText("Markus1234"))
        onView(withId(R.id.submit_button)).perform(click())
    }
}