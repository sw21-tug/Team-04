package com.example.traveltogether

import android.content.Intent
import android.view.Gravity
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class EditPostTest {

    @Test //how to test UI elements
    fun checkpopupfragment() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.new_popup_fragment)).perform(click())
        onView(withId(R.id.title_edit)).perform(typeText("bus trip"))
        onView(withId(R.id.destination_text)).perform(typeText("Ohio"))
        onView(withId(R.id.starting_date_text)).perform(typeText("3.6.2020"))
        onView(withId(R.id.number_people_text)).perform(typeText("4"))
        onView(withId(R.id.duration_text)).perform(typeText("4 weeks"))
        onView(withId(R.id.description_text2)).perform(typeText("Trip with bus"))
    }
}