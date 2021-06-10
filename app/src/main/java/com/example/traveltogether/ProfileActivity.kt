package com.example.traveltogether

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    lateinit var descriptionText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        showEditTextDialog()




        val user = FirebaseAuth.getInstance().currentUser
        editTextTextPersonName.text = FirebaseAuth.getInstance().currentUser?.displayName

        descriptionText = findViewById(R.id.description_text)
        descriptionText.movementMethod = ScrollingMovementMethod()
        if(user?.photoUrl != null) {
            Glide.with(this)
                .load(user.photoUrl)
                .into(profile_picture)
        }
        // set on-click listener
        profile_picture.setOnClickListener {
            val popup = PopupMenu(this, profile_picture)
            popup.inflate(R.menu.test)
            popup.setOnMenuItemClickListener {
                when (it.title) {
                    getString(R.string.take_picture) -> takePictureIntent()
                    getString(R.string.choose_from_library) -> takeImageFormGalleryIntent()
                    else -> Toast.makeText(this, "Item: " + it.title, Toast.LENGTH_SHORT).show()
                }
                true
            }
            popup.show()
        }
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference.child("users").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
        firebaseref.child("Description").addValueEventListener (object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val description = dataSnapshot.value.toString()
                if (description != "null")
                    descriptionText.setText(description)
            }
            override fun onCancelled (databaseError: DatabaseError) {
                //errors
            }
        })

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.profile_name)
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        //closeKeyBoard()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(intentFor<MainActivity>().newTask().clearTask())
    }

    private fun showEditTextDialog() {
        val saveDescription = findViewById<Button>(R.id.save_description)
        saveDescription.setOnClickListener {
            val text : EditText = findViewById<EditText>(R.id.description_text)
            val firebasereal = FirebaseDatabase.getInstance()
            val firebaseref = firebasereal.getReference()
            firebaseref.child("users").child(FirebaseAuth.getInstance().currentUser.uid).child("Description").setValue(text.text.toString())

            Toast.makeText(
                this,
                "Saved Description",
                Toast.LENGTH_SHORT
            ).show()

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
            Toast.makeText(this, getString(R.string.update_success_text), Toast.LENGTH_SHORT)
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
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}

