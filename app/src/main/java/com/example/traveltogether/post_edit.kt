package com.example.traveltogether

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [post_edit.newInstance] factory method to
 * create an instance of this fragment.
 */
class post_edit : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_post_edit, container, false)
        val firebasereal = FirebaseDatabase.getInstance()
        val firebaseref = firebasereal.reference.child("post")
        val title_edit = view.findViewById<View>(R.id.title_edit) as EditText
        val number_people_text = view.findViewById<View>(R.id.number_people_text) as EditText
        val starting_date_text = view.findViewById<View>(R.id.starting_date_text) as EditText
        val description_text = view.findViewById<View>(R.id.description_text2) as EditText
        val destination_text = view.findViewById<View>(R.id.destination_text) as EditText


        firebaseref.child("destination").addValueEventListener (object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val destination = dataSnapshot.value.toString()
                if (destination != "null")
                    destination_text.setText(destination)
            }
            override fun onCancelled (databaseError: DatabaseError) {
                //errors
            }
        })
        firebaseref.child("title").addValueEventListener (object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val title = dataSnapshot.value.toString()
                if (title != "null")
                    title_edit.setText(title)
            }
            override fun onCancelled (databaseError: DatabaseError) {
                //errors
            }
        })
        firebaseref.child("num_of_people").addValueEventListener (object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val people = dataSnapshot.value.toString()
                if (people != "null")
                    number_people_text.setText(people)
            }
            override fun onCancelled (databaseError: DatabaseError) {
                //errors
            }
        })
        firebaseref.child("start_date").addValueEventListener (object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val start_date = dataSnapshot.value.toString()
                if (start_date != "null")
                    starting_date_text.setText(start_date)
            }
            override fun onCancelled (databaseError: DatabaseError) {
                //errors
            }
        })
        firebaseref.child("description").addValueEventListener (object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val description = dataSnapshot.value.toString()
                if (description != "null")
                    description_text.setText(description)
            }
            override fun onCancelled (databaseError: DatabaseError) {
                //errors
            }
        })

        val button_save : Button = view.findViewById(R.id.button3)
        button_save.setOnClickListener {
            val firebasereal = FirebaseDatabase.getInstance()
            val firebaseref = firebasereal.getReference()
            firebaseref.child("post").child("destination").setValue(destination_text.text.toString())
            firebaseref.child("post").child("start_date").setValue(starting_date_text.text.toString())
            firebaseref.child("post").child("num_of_people").setValue(number_people_text.text.toString())
            firebaseref.child("post").child("title").setValue(title_edit.text.toString())
            firebaseref.child("post").child("description").setValue(description_text.text.toString())
            findNavController().navigate(R.id.action_post_edit_to_saved_post_fragment)        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment post_edit.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                post_edit().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}