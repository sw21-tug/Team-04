package com.example.traveltogether

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.content.DialogInterface
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class ChangePassActivity : AppCompatActivity() {
    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        if (successfully)
            goBackToSetting();
    }

    val alert : String = "Alert";
    val success : String = "Success";
    var successfully : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)

        val submit_btn: Button = findViewById<View>(R.id.submit_button) as Button
        val old_pass: EditText = findViewById<View>(R.id.TextPassword_Old) as EditText
        val new_pass: EditText = findViewById<View>(R.id.TextPassword_new) as EditText
        val new_pass_again: EditText = findViewById<View>(R.id.TextPassword_new_again) as EditText

        print(old_pass.text);
        submit_btn.setOnClickListener {
            if (old_pass.text.toString().isEmpty())
                PopUp(alert, "Please enter the old password");
            else if( new_pass.text.isEmpty() )
                PopUp(alert, "Please enter the new password");
            else if(new_pass_again.text.isEmpty())
                PopUp(alert, "Please re-enter the new password");
            else if(!new_pass.text.toString().equals(new_pass_again.text.toString()))
                PopUp(alert, "Please enter the same new password!");
            else if(old_pass.text.toString().equals(new_pass.text.toString()))
                PopUp(alert,"Your old password and new one are the same, please change it!");
            else
            {
                //TODO check if old password is same as in database
                //TODO update the database with the new password!
                successfully = true;
                //after successfully changing it
                PopUp(success, "You have changed your password successfully");
            }

        }
    }

    fun PopUp(title: String, msg: String){
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(title)
            setMessage(msg)
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            show()
        }
    }

    fun goBackToSetting()
    {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }
}