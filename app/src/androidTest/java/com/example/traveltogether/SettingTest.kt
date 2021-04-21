package com.example.traveltogether

import android.net.sip.SipSession
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import java.util.regex.Pattern.matches

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
    fun checkChangingPassButton()
    {
        onView(withId(R.id.change_pass)).perform(click())
        val originalActivityState = activityRule.scenario.state
    }
}

