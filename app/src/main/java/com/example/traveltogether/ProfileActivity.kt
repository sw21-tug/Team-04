package com.example.traveltogether

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class ProfileActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        showEditTextDialog()
        // set on-click listener
        edit_picture_button.setOnClickListener {
            /*val popup = PopupMenu(this, edit_picture_button)
            popup.inflate(R.menu.test)
            popup.setOnMenuItemClickListener {
                Toast.makeText(this, "Item: " + it.title, Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()*/
            takePictureIntent()
        }

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.profile_name)
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(intentFor<MainActivity>().newTask().clearTask())
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

    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            pictureIntent->pictureIntent.resolveActivity(this?.packageManager!!).also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
                .reference
                .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        progress_bar_pic.visibility = View.VISIBLE
        val upload = storageRef.putBytes(image)
        upload.addOnCompleteListener { uploadTask ->
            progress_bar_pic.visibility = View.INVISIBLE
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{ urlTask ->
                    urlTask.result?.let{
                        imageUri = it
                        profile_picture.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

}

