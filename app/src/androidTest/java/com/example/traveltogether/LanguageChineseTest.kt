package com.example.traveltogether

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LanguageChineseTest {

    @Before
    fun setup(){
        val activityScenario = ActivityScenario.launch(SignIn::class.java)
        onView(withId(R.id.language_button)).perform(click())
        // add Language as in menu to choose (i.e. "Русские")
        onView(withText("中國人")).perform(click())
    }

    @Test
    fun LogIn () {
        val activityScenario = ActivityScenario.launch(SignIn::class.java)
        // @string sign_in_text
        onView(withText("登陸註冊")).check(matches(isDisplayed()))
        // already correct
        onView(withText("改變語言")).check(matches(isDisplayed()))
    }

    @Test
    fun MainActivity () {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        // @string all_text
        onView(withId(R.id.all_post_fragment)).check(matches(withContentDescription("全部")))
        onView(withId(R.id.all_post_fragment)).perform(click())
        onView(withId(R.id.all_post_fragment)).check(matches(withContentDescription("全部")))

        // @string saved_text
        onView(withId(R.id.saved_post_fragment)).check(matches(withContentDescription("已保存")))
        onView(withId(R.id.saved_post_fragment)).perform(click())
        // @string saved_post_section_text
        onView(withText("保存的帖子部分")).check(matches(isDisplayed()))

        // @string chat_text
        onView(withId(R.id.chat_fragment)).check(matches(withContentDescription("聊天")))
        onView(withId(R.id.chat_fragment)).perform(click())
        onView(withId(R.id.chat_fragment)).check(matches(withContentDescription("聊天")))


        // @string new_text
        onView(withId(R.id.new_popup_fragment)).check(matches(withContentDescription("新的")))
        onView(withId(R.id.new_popup_fragment)).perform(click())
        onView(withId(R.id.new_popup_fragment)).check(matches(withContentDescription("新的")))


        // @string news_text
        onView(withId(R.id.news_fragment)).check(matches(withContentDescription("消息")))
        onView(withId(R.id.news_fragment)).perform(click())
        onView(withId(R.id.news_fragment)).check(matches(withContentDescription("消息")))

    }

    @Test
    fun menu () {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        // @string settings_text
        onView(withText("設定值")).check(matches(isDisplayed()))
        //@string profile_name
        onView(withText("輪廓")).check(matches(isDisplayed()))
        //@string logout_text
        onView(withText("登出")).check(matches(isDisplayed()))
    }
    @Test
    fun profile () {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        // @string profile_text
        onView(withText("輪廓")).perform(click())
        onView(withText("輪廓")).check(matches(isDisplayed()))
        // @string profile_description
        onView(withText("描述")).check(matches(isDisplayed()))
        // @string profile_edit_description
        onView(withText("編輯說明")).check(matches(isDisplayed()))
        // @string profile_edit_picture
        onView(withText("編輯圖片")).check(matches(isDisplayed()))
    }

    @Test
    fun chooseLanguage () {
        val activityScenario = ActivityScenario.launch(SignIn::class.java)
        onView(withId(R.id.language_button)).perform(click())
        // already correct
        onView(withText("Choose Language")).check(matches(isDisplayed()))
        // add Language as in menu to choose (i.e. "Русские")
        onView(withText("中國人")).check(matches(isDisplayed()))
        // already correct
        onView(withText("English")).check(matches(isDisplayed())).perform(click())
        onView(withText("Log In/Sign Up")).check(matches(isDisplayed()))
    }
}