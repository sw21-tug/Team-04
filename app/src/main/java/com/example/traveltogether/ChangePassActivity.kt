package com.example.traveltogether

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class ChangePassActivity : AppCompatActivity() {
    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        if (successfully) {
            goBackToSetting();
        }
    }

    val alert : String = "Error";
    val success : String = "Success";
    var successfully : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)

        val submit_btn: Button = findViewById<View>(R.id.submit_button) as Button
        val old_pass: EditText = findViewById<View>(R.id.TextPassword_Old) as EditText
        val new_pass: EditText = findViewById<View>(R.id.TextPassword_new) as EditText
        val new_pass_again: EditText = findViewById<View>(R.id.TextPassword_new_again) as EditText

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
                var cred = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().currentUser.email,
                    old_pass.text.toString()
                );
                var user =  FirebaseAuth.getInstance().currentUser;
                if(user != null) {
                    user.reauthenticate(cred)
                        .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {
                                FirebaseAuth.getInstance().currentUser.updatePassword(new_pass.toString());
                                successfully = true;
                                PopUp(success, "You have changed your password successfully");
                            } else {
                                PopUp(alert, "old Password is wrong!");
                            }
                        })

                }


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
