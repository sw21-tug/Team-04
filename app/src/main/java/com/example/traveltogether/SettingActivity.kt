package com.example.traveltogether

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val change_pass_btn: Button = findViewById<View>(R.id.change_pass) as Button
        val delete_account_btn: Button = findViewById<View>(R.id.delete_account) as Button
        val allow_notif_switch: Switch = findViewById<View>(R.id.allow_notification) as Switch


        change_pass_btn.setOnClickListener {
            val intent = Intent(this, ChangePassActivity::class.java)
            startActivity(intent)
        }

        delete_account_btn.setOnClickListener{
           areYouSurePopUp()
        }

        allow_notif_switch.setOnClickListener {
            //TODO activate notifications
        }
    }


    fun areYouSurePopUp() {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Delete Account")
        alertDialog.setMessage("Are you sure you want to delete your account?")

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes"
        ) {
            //TODO delete Account from fireBase
            //TODO logout!
            // Goto logInPage!!
            dialog, which -> dialog.dismiss()
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No"
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }
}