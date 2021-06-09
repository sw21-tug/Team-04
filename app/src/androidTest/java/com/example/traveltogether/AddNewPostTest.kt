package com.example.traveltogether

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class AddNewPostTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var loginUser: LoginUser

    @Before
    fun setup() {
        loginUser = LoginUser("test@gmail.com", "Name", "12345678", "")
        loginUser.signIn()
        onView(withId(R.id.new_popup_fragment)).perform(click())
    }


    @Test
    fun checkPopupFragment() {
        onView(withId(R.id.title_edit_post)).check(matches(isDisplayed()))
        onView(withId(R.id.destination_text_post)).check(matches(isDisplayed()))
        onView(withId(R.id.starting_date_post_text)).check(matches(isDisplayed()))
        onView(withId(R.id.starting_date_post_text)).check(matches(isDisplayed()))
        onView(withId(R.id.number_people_post_text)).check(matches(isDisplayed()))
        onView(withId(R.id.description_post_text)).check(matches(isDisplayed()))
    }

    private fun getRandomString(size: Int ) : String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..size)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    @Test
    fun addPost() {
        val testTitle = getRandomString(10)
        val testLocation = getRandomString(5)
        val testDescription = getRandomString(15)
        val numberOfPeople = kotlin.random.Random.nextInt(0, 10).toString()

        onView(withId(R.id.title_edit_post)).perform(typeText(testTitle))
        onView(withId(R.id.destination_text_post)).perform(typeText(testLocation))
        onView(withHint(R.string.starting_date)).perform(click())
        onView(withText("OK")).perform(click())
        onView(withId(R.id.number_people_post_text)).perform(typeText(numberOfPeople))
        pressBack()
        onView(withId(R.id.description_post_text)).perform(typeText(testDescription))
        pressBack()
        onView(withId(R.id.button_save_new_post)).perform(click())

        onView(withHint(R.string.title)).check(matches(isDisplayed()))
        onView(withHint(R.string.destination)).check(matches(isDisplayed()))
        onView(withHint(R.string.number_people)).check(matches(isDisplayed()))
        onView(withHint(R.string.starting_date)).check(matches(isDisplayed()))
        onView(withHint(R.string.ending_date)).check(matches(isDisplayed()))
    }

    @Test
    fun clearInput() {
        val testTitle = getRandomString(10)
        val testLocation = getRandomString(5)
        val testDescription = getRandomString(15)
        val numberOfPeople = kotlin.random.Random.nextInt(0, 10).toString()

        onView(withId(R.id.title_edit_post)).perform(typeText(testTitle))
        onView(withId(R.id.destination_text_post)).perform(typeText(testLocation))
        onView(withHint(R.string.ending_date)).perform(click())
        onView(withText("OK")).perform(click())
        onView(withId(R.id.number_people_post_text)).perform(typeText(numberOfPeople))
        pressBack()
        onView(withId(R.id.description_post_text)).perform(typeText(testDescription))
        pressBack()
        onView(withId(R.id.delete_button)).perform(click())

        onView(withHint(R.string.title)).check(matches(isDisplayed()))
        onView(withHint(R.string.destination)).check(matches(isDisplayed()))
        onView(withHint(R.string.number_people)).check(matches(isDisplayed()))
        onView(withHint(R.string.starting_date)).check(matches(isDisplayed()))
        onView(withHint(R.string.ending_date)).check(matches(isDisplayed()))
    }
}