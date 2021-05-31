package com.example.traveltogether

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.*
import java.util.*


class SignIn : AppCompatActivity() {

    private val RC_SIGN_IN = 1
        get() = field

    private val signInProviders = listOf(AuthUI.IdpConfig.EmailBuilder()
        .setAllowNewAccounts(true)
        .setRequireName(true)
        .build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)

        val changeLanguageButton : Button = findViewById(R.id.language_button)
        changeLanguageButton.setOnClickListener { showChangeLang() }

        account_sign_in.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.Theme_TravelTogether)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)

        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun showChangeLang() {
        val listItems = arrayOf("English", "Русские", "中國人" )
        val mBuilder = AlertDialog.Builder(this@SignIn)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialog, which ->
            if (which == 0) {
                setLocate("en")
                recreate()
            }
            if (which == 1) {
                setLocate("ru")
                recreate()
            }
            if (which == 2) {
                setLocate("zh")
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog ( "Setting up your account" )
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val firebasereal = FirebaseDatabase.getInstance()
                val firebaseref = firebasereal.getReference()
                firebaseref.child("users").child(FirebaseAuth.getInstance().currentUser.uid).child("Description")
                if (!firebaseUser?.isEmailVerified!!)
                    firebaseUser.sendEmailVerification()
                startActivity(intentFor<MainActivity>().newTask().clearTask())
                progressDialog.dismiss()
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) {
                    Toast.makeText(this@SignIn, getString(R.string.sign_in_canceled_text), Toast.LENGTH_SHORT).show()
                    return
                }
                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        Toast.makeText(this@SignIn, "No Network.", Toast.LENGTH_SHORT).show()
                    ErrorCodes.UNKNOWN_ERROR ->
                        Toast.makeText(this@SignIn, "Unknown error", Toast.LENGTH_SHORT).show()
                    else ->
                        Toast.makeText(this@SignIn, "Unknown error", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}