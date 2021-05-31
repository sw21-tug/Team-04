package com.example.traveltogether

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.traveltogether.R.id
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.hamcrest.Matcher
import org.junit.Assert
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
class ProfileDescriptionTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileActivity::class.java)

   private lateinit var loginUser: LoginUser

    @Before
    fun setup() {

        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..20)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
        loginUser = LoginUser("markus123@gmail.com", "Markus", "markus123", randomString)
        loginUser.signIn()
    }

    @Test
    fun checkIfCorrectDescriptionDisplayed() {
        val dataSnapshot = await(FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("Description").get())
        onView(withId(R.id.description_text)).check(matches(withText(dataSnapshot.value.toString())))
    }

    @Test
    fun changeDescriptionTest() {
        setup()
        onView(withId(id.save_description)).perform(click())
        onView(withId(id.et_editView)).perform(typeText(loginUser.description))
        onView(withText("OK")).perform(click())

        val dataSnapshot = await(FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("Description").get())
        Assert.assertEquals(loginUser.description, dataSnapshot.value.toString())
    }

    @Test
    fun descriptionAfterRejoinActivity() {
        setup()
        onView(withId(id.save_description)).perform(click())
        onView(withId(id.et_editView)).perform(typeText(loginUser.description))
        onView(withText("OK")).perform(click())

        val oldText = getText(withId(R.id.description_text))
        pressBack()
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout)).check(matches(DrawerMatchers.isOpen(Gravity.LEFT)))
        onView(withText("Profile")).perform(click())

        val newText = getText(withId(R.id.description_text))

        Assert.assertEquals(oldText, newText)

    }

    private fun getText(matcher: Matcher<View?>?): String? {
        val stringHolder = arrayOf<String?>(null)
        onView(matcher).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "getting text from a TextView"
            }

            override fun perform(uiController: UiController?, view: View) {
                val tv = view as TextView
                stringHolder[0] = tv.text.toString()
            }
        })
        return stringHolder[0]
    }

}