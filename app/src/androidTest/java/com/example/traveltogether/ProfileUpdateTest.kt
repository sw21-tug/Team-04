package com.example.traveltogether

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.Gravity
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_profile.*
import org.junit.Assert
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
class ProfileUpdateTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileActivity::class.java)

    private var refBitmap: Bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888)

    fun setup() {
        Assert.assertNotNull(FirebaseAuth.getInstance().currentUser)
        val canvas = Canvas(refBitmap)
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        canvas.drawColor(color)
    }

    @Test
    fun checkIfPhotoUriUpdates() {
        //FirebaseAuth.getInstance().signInWithEmailAndPassword("test123@gmail.com", "12345678")
        setup()
        //ActivityScenario.launch(ProfileActivity::class.java)
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(null)
            .build()
        val user = FirebaseAuth.getInstance().currentUser
        user?.updateProfile(request)
        Thread.sleep(2000)
        Assert.assertNull(FirebaseAuth.getInstance().currentUser?.photoUrl)

        val scenario: ActivityScenario<ProfileActivity>? = activityRule.scenario
        scenario?.onActivity { activity -> run { activity.uploadImageAndSaveUri(refBitmap) } }
        scenario?.onActivity { activity -> activity.profile_picture.setImageBitmap(refBitmap)}

        Thread.sleep(4000)

        Assert.assertNotNull(FirebaseAuth.getInstance().currentUser?.photoUrl)
    }

    @Test
    fun checkIfPhotoBitmapUpdates() {
        setup()
        //ActivityScenario.launch(ProfileActivity::class.java)
        Thread.sleep(2000)

        val scenario: ActivityScenario<ProfileActivity>? = activityRule.scenario
        var oldBitmap : Bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888)
        scenario?.onActivity { activity -> oldBitmap = activity.profile_picture.drawable.toBitmap()}

        scenario?.onActivity { activity -> run { activity.uploadImageAndSaveUri(refBitmap) } }
        scenario?.onActivity { activity -> activity.profile_picture.setImageBitmap(refBitmap) }

        Thread.sleep(4000)

        pressBack()
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isOpen(Gravity.LEFT)))

        onView(withText("Profile")).perform(click())
        Thread.sleep(2000)

        scenario?.onActivity { activity ->
            Assert.assertFalse(equals(oldBitmap, activity.profile_picture.drawable.toBitmap()))
        }
    }

    private fun equals(bmp1: Bitmap, bmp2: Bitmap): Boolean {
        if (bmp1.width != bmp2.width || bmp1.height != bmp2.height)
            return false
        for (x in 0 until bmp1.width) {
            for (y in 0 until bmp1.height) {
                if (bmp1.getPixel(x, y) != bmp2.getPixel(x, y)) {
                    return false
                }
            }
        }
        return true
    }

}