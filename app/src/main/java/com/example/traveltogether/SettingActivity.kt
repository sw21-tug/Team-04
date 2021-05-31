package com.example.traveltogether

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.*
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import java.util.*


class SettingActivity : AppCompatActivity() {

    private var triggerRecreate : Boolean = false

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.settings_text)
        actionbar.setDisplayHomeAsUpEnabled(true)


        val change_pass_btn = findViewById<Button>(R.id.change_pass)
        val delete_account_btn = findViewById<Button>(R.id.delete_account)
        val switch_theme = findViewById<Switch>(R.id.switch_to_dark)
        val change_username = findViewById<Button>(R.id.change_username)
        val old_usrname = findViewById<EditText>(R.id.editTextChangeUsername)

        old_usrname.setText(FirebaseAuth.getInstance().currentUser?.displayName)
        val sharedPreferences = getSharedPreferences(
                "sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)
        switch_theme.isChecked = isDarkModeOn

        switch_theme.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkModeOn", true)
                editor.apply()
                switchLanguage()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkModeOn", false)
                editor.apply()
                switchLanguage()
            }

        })

        change_pass_btn.setOnClickListener {
            val intent = Intent(this, ChangePassActivity::class.java)
            startActivity(intent)
        }

        delete_account_btn.setOnClickListener{
           areYouSurePopUp()
        }

        switch_theme.setOnClickListener {
        }
        change_username.setOnClickListener{
            if (old_usrname.text.toString().isEmpty())
                Toast.makeText(this, "Please fill in new username!", Toast.LENGTH_LONG).show()
            else {
                setUserProfileDisplayName(old_usrname.text.toString())
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun switchLanguage() {
        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "en")!!
        setLocate(language)
        recreate()
    }

    private fun setLocate(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }

    private fun setUserProfileDisplayName(name: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val request = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        user?.updateProfile(request)?.addOnSuccessListener {
            Toast.makeText(this, getString(R.string.update_success_text), Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener {
            Toast.makeText(
                this,
                "Failed to update Username",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun areYouSurePopUp() {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(getString(R.string.delete_acc_text))
        alertDialog.setMessage(getString(R.string.delete_acc_question))

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes_text)
        ) {
            dialog, which ->
            FirebaseAuth.getInstance().currentUser?.delete()
            startActivity(intentFor<SignIn>().newTask().clearTask())

            finish()
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no_text)
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(intentFor<MainActivity>().newTask().clearTask())
    }
}