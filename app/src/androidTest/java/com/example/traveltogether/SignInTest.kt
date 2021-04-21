package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
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
class LoginSignupTest {

    @get:Rule
    val activityRule = ActivityTestRule(SignIn::class.java)

    @Before
    fun setup(){
        FirebaseAuth.getInstance().signOut()
    }
    @Test
    fun checkSignUpButton() {
        val loginSignUpButton = withId(R.id.account_sign_in)
        onView(loginSignUpButton).check(matches(isDisplayed()))
        onView(loginSignUpButton).check(matches(withText("Log In/Sign Up")))

    }

    @Test
    fun checkSignUpIntent () {
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

        onView(withText("Sign in")).check(matches(isDisplayed()));
    }

    @Test
    fun login () {
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

        onView(withId(R.id.email)).perform(typeText("markus1234@gmail.com"))

        onView(withId(R.id.button_next)).perform(click())

        onView(withId(R.id.password)).perform(typeText("Markus1234"))
        onView(withId(R.id.button_done)).perform(click())

        assertNotNull(FirebaseAuth.getInstance().currentUser)

    }
}