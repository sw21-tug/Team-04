package com.example.traveltogether

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask


class SettingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.settings_text)
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


        val change_pass_btn: Button = findViewById<View>(R.id.change_pass) as Button
        val delete_account_btn: Button = findViewById<View>(R.id.delete_account) as Button
        val allow_notif_switch: Switch = findViewById<View>(R.id.allow_notification) as Switch
        val change_username: Button = findViewById<View>(R.id.change_username) as Button
        val old_usrname: EditText = findViewById<View>(R.id.editTextChangeUsername) as EditText

        old_usrname.setText(FirebaseAuth.getInstance().currentUser?.displayName)

        change_pass_btn.setOnClickListener {
            val intent = Intent(this, ChangePassActivity::class.java)
            startActivity(intent)
        }

        delete_account_btn.setOnClickListener{
           areYouSurePopUp()
        }

        allow_notif_switch.setOnClickListener {
        }
        change_username.setOnClickListener{
            if (old_usrname.text.toString().isEmpty())
                Toast.makeText(this, "Please fill in new username!", Toast.LENGTH_LONG).show()
            else {
                setUserProfileDisplayName(old_usrname.text.toString())
            }
        }
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