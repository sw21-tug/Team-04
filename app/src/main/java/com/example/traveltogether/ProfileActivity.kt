package com.example.traveltogether

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        showEditTextDialog()
        // set on-click listener
        edit_picture_button.setOnClickListener {
            val popup = PopupMenu(this, edit_picture_button)
            popup.inflate(R.menu.test)
            popup.setOnMenuItemClickListener {
                Toast.makeText(this, "Item: " + it.title, Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()
        }

    }

    private fun showEditTextDialog()
    {
        edit_description.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et_editView)
            with(builder){
                setTitle("Add your profile description here")
                setPositiveButton("OK"){ dialog, which->
                    description_text.text = editText.text.toString()
                }
                setNegativeButton("Cancel"){ dialog, which->
                    Log.d("Main", "negative button clicked")
                }
                setView(dialogLayout)
                show()
            }

        }
    }

}