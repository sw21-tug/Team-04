package com.example.traveltogether

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class LoginSignUpTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignIn::class.java)


    private lateinit var loginUser: LoginUser

    @Before
    fun setup() {
        loginUser = LoginUser("test1@gmail.com", "Name", "12345678", "Hallo")
        loginUser.signIn()
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

        val scenario: ActivityScenario<SignIn>? = activityRule.scenario
        scenario?.onActivity { activity -> run { assertEquals(intent.data, activity.intent.data) } }
        onView(withId(R.id.button_next)).check(matches(isDisplayed()));
    }

    @Test
    fun login () {
        setup()
        loginUser.signOut()
        assertNull(FirebaseAuth.getInstance().currentUser)
        val loginSignUpButton = withId(R.id.account_sign_in)

        onView(loginSignUpButton).perform(click())
        onView(withId(R.id.email)).perform(typeText("test1@gmail.com"))
        onView(withId(R.id.button_next)).perform(click())
        onView(withId(R.id.password)).perform(typeText("12345678"))
        onView(withId(R.id.button_done)).perform(click())

        assertNotNull(FirebaseAuth.getInstance().currentUser)
    }
}