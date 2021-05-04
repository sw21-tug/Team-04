package com.example.traveltogether

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar



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

        account_sign_in.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.Theme_TravelTogether)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)

        }
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
                    Toast.makeText(this@SignIn, "Sign in cancelled.", Toast.LENGTH_SHORT).show()
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