package com.example.traveltogether

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditPostTest {

    @Test //how to test UI elements
    fun checkpopupfragment() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.new_popup_fragment)).perform(click())
        onView(withId(R.id.title_edit_post)).perform(typeText("bus trip"))
        onView(withId(R.id.destination_text_post)).perform(typeText("Ohio"))
        onView(withId(R.id.starting_date_post_text)).perform(typeText("03/06/2020"))
        onView(withId(R.id.number_people_post_text)).perform(typeText("4"))
        onView(withId(R.id.end_date_post_text)).perform(typeText("03/06/2020"))
        onView(withId(R.id.description_post_text)).perform(typeText("Trip with bus"))
    }
}