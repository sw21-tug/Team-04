package com.example.traveltogether

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.Gravity
import android.widget.EditText
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.awaitAll
import org.jetbrains.anko.doAsync
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ChangeUsernameTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SettingActivity::class.java)

    private lateinit var loginUser: LoginUser

    private lateinit var username: String

    @Before
    fun setup() {
        loginUser = LoginUser("changeusernametest@gmail.com", "changeusernametest", "changeusernametest", "changeusernametest")
        loginUser.signIn()
        username = getRandomString(10)
    }

    @Test
    fun checkIfDisplayUsernameUI() {
        onView(withId(R.id.change_username)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextChangeUsername)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfDisplayUsernameUpdates() {

        val request = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()
        val user = FirebaseAuth.getInstance().currentUser
        user?.updateProfile(request)?.let { await(it) }
        Assert.assertEquals(username, FirebaseAuth.getInstance().currentUser?.displayName)
    }

    @Test
    fun checkIfDisplayUsernameUpdatesTyping() {
        val scenario: ActivityScenario<SettingActivity>? = activityRule.scenario
        scenario?.onActivity { activity -> run {
            val editText: EditText = activity.findViewById<EditText>(R.id.editTextChangeUsername) as EditText
            editText.setText("")
        }}

        onView(withId(R.id.editTextChangeUsername)).perform(ViewActions.typeText(username))
        onView(withId(R.id.change_username)).perform(click())
        Thread.sleep(2000)
        Assert.assertEquals(username, FirebaseAuth.getInstance().currentUser?.displayName)
    }


    private fun getRandomString(size: Int ) : String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..size)
                .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
    }
}