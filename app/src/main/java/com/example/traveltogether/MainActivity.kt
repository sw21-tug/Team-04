package com.example.traveltogether

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.db.NULL
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sign_out = findViewById<Button>(R.id.button_sign_out)
        sign_out.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(intentFor<SignIn>().newTask().clearTask())
                    finish()
                }
        }
    }
}