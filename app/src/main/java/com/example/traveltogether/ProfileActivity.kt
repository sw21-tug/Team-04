package com.example.traveltogether

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 100
    private val REQUEST_IMAGE_GALLERY = 101
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        showEditTextDialog()
        val user = FirebaseAuth.getInstance().currentUser
        editTextTextPersonName.text = FirebaseAuth.getInstance().currentUser?.displayName

        if(user?.photoUrl != null) {
            Glide.with(this)
                .load(user.photoUrl)
                .into(profile_picture)
        }
        // set on-click listener
        edit_picture_button.setOnClickListener {
            val popup = PopupMenu(this, edit_picture_button)
            popup.inflate(R.menu.test)
            popup.setOnMenuItemClickListener {
                when (it.title) {
                    "Take a picture" -> takePictureIntent()
                    "Choose from Library" -> takeImageFormGalleryIntent()
                    else -> Toast.makeText(this, "Item: " + it.title, Toast.LENGTH_SHORT).show()
                }
                true
            }
            popup.show()
        }
        val actionbar = supportActionBar
        actionbar!!.title = "Profile"
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

    private fun showEditTextDialog() {
        edit_description.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et_editView)
            with(builder) {
                setTitle("Add your profile description here")
                setPositiveButton("OK") { dialog, which ->
                    description_text.text = editText.text.toString()
                }
                setNegativeButton("Cancel") { dialog, which ->
                    Log.d("Main", "negative button clicked")
                }
                setView(dialogLayout)
                show()
            }

        }
    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(this.packageManager!!).also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun takeImageFormGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
            profile_picture.setImageBitmap(imageBitmap)
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            val imageUri = data?.data
            profile_picture.setImageURI(imageUri)
            uploadImageAndSaveUri(profile_picture.drawable.toBitmap())
        }
    }

    fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val uid = FirebaseAuth.getInstance().currentUser?.uid;
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("profilePictures/${uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        progress_bar_pic.visibility = View.VISIBLE
        val upload = storageRef.putBytes(image)
        upload.addOnCompleteListener { uploadTask ->
            progress_bar_pic.visibility = View.INVISIBLE
            if (uploadTask.isSuccessful) {
                getDownloadUrl(storageRef)
            }
        }
    }

    private fun getDownloadUrl(reference: StorageReference) {
        reference.downloadUrl
            .addOnSuccessListener { uri ->
                Log.d("DEBUG", "onSuccess: $uri")
                setUserProfileUrl(uri)
            }
    }

    private fun setUserProfileUrl(uri: Uri) {
        val user = FirebaseAuth.getInstance().currentUser
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()
        user?.updateProfile(request)?.addOnSuccessListener {
            Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT)
                .show()
            Log.d("DEBUG", "update profile")
        }?.addOnFailureListener {
            Toast.makeText(
                this,
                "Profile image failed...",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}

