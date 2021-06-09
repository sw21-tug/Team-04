package com.example.traveltogether

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.type.DateTime
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [new_popup_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class new_popup_fragment : Fragment() {

    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var titlePost: EditText
    private lateinit var destinationPost: EditText
    private lateinit var numOfPersonsPost: EditText
    private lateinit var startDatePost: EditText
    private lateinit var endDatePost: EditText
    private lateinit var descriptionPost: EditText

    var calendar: Calendar = Calendar.getInstance()

    private var param1: String? = null
    private var param2: String? = null

    private val myFormat = "dd/MM/yyyy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_new_popup_fragment, container,
                false)
        val activity = activity as Context
        
        saveButton = view.findViewById(R.id.button_save_new_post) as Button
        titlePost = view.findViewById(R.id.title_edit_post)
        destinationPost = view.findViewById(R.id.destination_text_post)
        numOfPersonsPost = view.findViewById(R.id.number_people_post_text)
        startDatePost = view.findViewById(R.id.starting_date_post_text)
        endDatePost = view.findViewById(R.id.end_date_post_text)
        descriptionPost = view.findViewById(R.id.description_post_text)
        deleteButton = view.findViewById(R.id.delete_button)

        saveButton.setOnClickListener {
            val userPost: UserPost
            if(checkFields()) {
                userPost = getUserPost()
                userPost.post()
                clearFields()
                Toast.makeText(activity, "Successfully added Post " + userPost.Title, Toast.LENGTH_LONG).show()
            }
        }


        numOfPersonsPost.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(!numOfPersonsPost.text.isEmpty()) {
                    val v = numOfPersonsPost.text.toString().toInt();
                    if(v < 2)
                        numOfPersonsPost.setText("2")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        deleteButton.setOnClickListener{
            clearFields()
        }

        val startDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
                if(calendar.time.before(Date()))
                {
                    calendar.set(Calendar.YEAR, LocalDate.now().year)
                    calendar.set(Calendar.MONTH, LocalDate.now().monthValue - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, LocalDateTime.now().dayOfMonth)
                }
                startDatePost.setText(sdf.format(calendar.time))
                endDatePost.setText(sdf.format(calendar.time))
            }

        val endDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
                endDatePost.setText(sdf.format(calendar.time))
            }

        startDatePost.setOnClickListener {
            DatePickerDialog(activity,
                startDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        endDatePost.setOnClickListener {
            DatePickerDialog(activity,
                endDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        return view
    }

    private fun clearFields() {
        titlePost.setText("")
        descriptionPost.setText("")
        destinationPost.setText("")
        numOfPersonsPost.setText("")
        startDatePost.setText("")
        endDatePost.setText("")
    }

    private fun checkFields(): Boolean {
        if(titlePost.text.isEmpty() || destinationPost.text.isEmpty() || startDatePost.text.isEmpty()
            || endDatePost.text.isEmpty() || descriptionPost.text.isEmpty()) {

            Toast.makeText(activity, "Please fill in all fields!", Toast.LENGTH_LONG).show()
            return false
        }
        val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
        val startDate: Long = sdf.parse(startDatePost.text.toString())?.time!!
        val endDate: Long = sdf.parse(endDatePost.text.toString())?.time!!

        if(startDate > endDate) {
            Toast.makeText(activity, "You messed up the dates!", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    @SuppressLint("SimpleDateFormat")
    private fun getUserPost(): UserPost {

        val title = titlePost.text.toString()
        val destination = destinationPost.text.toString()
        val numberOfPerson = if (numOfPersonsPost.text.isNotEmpty()) numOfPersonsPost.text.toString().toLong() else 0

        val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
        val startDate: Long = sdf.parse(startDatePost.text.toString())?.time!!
        val endDate: Long = sdf.parse(endDatePost.text.toString())?.time!!
        val description = descriptionPost.text.toString()

        return UserPost(FirebaseAuth.getInstance().currentUser?.uid.toString(), "",
            System.currentTimeMillis(), title, destination,
            startDate, endDate, numberOfPerson, description, null, null, null)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment post_fragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            new_popup_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}