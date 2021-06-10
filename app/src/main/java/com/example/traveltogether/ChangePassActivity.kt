package com.example.traveltogether

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast


class ChangePassActivity : AppCompatActivity() {
    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        if (successfully) {
            goBackToSetting();
        }
    }

    var alert : String = ""
    var success : String = ""
    var successfully : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)
        alert = getString(R.string.error_text)
        success = getString(R.string.success_text)
        val submit_btn: Button = findViewById<View>(R.id.submit_button) as Button
        val old_pass: EditText = findViewById<View>(R.id.TextPassword_Old) as EditText

        val new_pass: EditText = findViewById<View>(R.id.TextPassword_new) as EditText
        val new_pass_again: EditText = findViewById<View>(R.id.TextPassword_new_again) as EditText



        submit_btn.setOnClickListener {
            if (old_pass.text.toString().isEmpty())
                PopUp(alert, getString(R.string.enter_old_pwd_text));
            else if( new_pass.text.isEmpty() )
                PopUp(alert, getString(R.string.enter_new_pwd_text));
            else if(new_pass_again.text.isEmpty())
                PopUp(alert, getString(R.string.reenter_pwd_text));
            else if(!new_pass.text.toString().equals(new_pass_again.text.toString()))
                PopUp(alert, getString(R.string.same_new_pwd_text));
            else if(old_pass.text.toString().equals(new_pass.text.toString()))
                PopUp(alert,getString(R.string.cant_be_same_pwd_text));
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
                                FirebaseAuth.getInstance().currentUser?.updatePassword(new_pass.text.toString())
                                    ?.addOnCompleteListener { task_ ->
                                        if (task_.isSuccessful) {
                                            successfully = true;
                                            PopUp(
                                                success,
                                                getString(R.string.success_pwd_change_text)
                                            );
                                        } else {
                                            PopUp(alert, getString(R.string.invalid_pwd_text));
                                        }
                                    };
                            } else {
                                PopUp(alert, getString(R.string.old_pwd_wrong_text));
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
            setPositiveButton(getString(R.string.ok_text), DialogInterface.OnClickListener(function = positiveButtonClick))
            show()
        }
    }

    fun goBackToSetting()
    {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(intentFor<MainActivity>().newTask().clearTask())
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