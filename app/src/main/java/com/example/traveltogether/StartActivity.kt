package com.example.traveltogether

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity
import java.util.*


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        setTheme()

        if (FirebaseAuth.getInstance().currentUser == null)
            startActivity<SignIn>()
        else
            startActivity<MainActivity>()
        finish()
    }
    fun setLocate(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }

    fun loadLocate () {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocate(language.toString())
    }

    @SuppressLint("CommitPrefEdits")
    fun setTheme() {
        // Saving state of our app
        // using SharedPreferences

        // Saving state of our app
        // using SharedPreferences
        val sharedPreferences = getSharedPreferences(
                "sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isDarkModeOn = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false)

        // When user reopens the app
        // after applying dark/light mode

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

}