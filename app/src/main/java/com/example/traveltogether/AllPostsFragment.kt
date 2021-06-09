package com.example.traveltogether

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [settings_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class all_post_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var posts: MutableList<UserPost>
    private lateinit var tempPost: MutableList<UserPost>
    private lateinit var adapter : PostsAdapter
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var calendar: Calendar = Calendar.getInstance()
    private val myFormat = "dd/MM/yyyy"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_all_post_fragment, container, false)
        val activity = activity as Context
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        posts = mutableListOf()
        tempPost = mutableListOf()

        if (container != null) {
            adapter = PostsAdapter(container.context, tempPost)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(container.context)
            posts.clear()

            var firebase = FirebaseDatabase.getInstance()
            var firebaseReference = firebase.reference.child("posts")

            firebaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    posts.clear()
                    tempPost.clear()
                    for (snapshot in dataSnapshot.children) {
                        val title = snapshot.child("title").value.toString()
                        val timePosted = snapshot.child("timePosted").value as Long
                        val destination = snapshot.child("destination").value.toString()
                        val description = snapshot.child("description").value.toString()
                        val endDate = snapshot.child("endDate").value as Long
                        val startDate = snapshot.child("startDate").value as Long
                        val numOfPeople = snapshot.child("numOfPeople").value as Long
                        val uid = snapshot.child("uid").value.toString()
                        val pid = snapshot.key.toString()

                        val userPost = UserPost(uid, pid, timePosted, title, destination, startDate, endDate, numOfPeople, description, null, null, null)
                        posts.add(userPost)
                    }
                    tempPost.addAll(posts)
                    posts.reverse()
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            val filterExpandButton = view.findViewById<Button>(R.id.filter_button_expand)
            val filterLayout = view.findViewById<ConstraintLayout>(R.id.filterLayout)

            filterExpandButton.setOnClickListener {
                if(filterLayout.visibility == View.VISIBLE) {
                    filterLayout.visibility = View.GONE
                    filterExpandButton.text = getString(R.string.expand_filter)
                } else {
                    filterLayout.visibility = View.VISIBLE
                    filterExpandButton.text = getString(R.string.close_filter)
                }
            }



            val filterStartButton = view.findViewById<Button>(R.id.button_filter)
            val titleEditText = view.findViewById<EditText>(R.id.filter_title)
            val destinationEditText = view.findViewById<EditText>(R.id.filter_destination)
            val startDateEditText = view.findViewById<EditText>(R.id.filter_startDate)
            val endDateEditText = view.findViewById<EditText>(R.id.filter_endDate)

            val startDateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)

                    val startDate: Long = calendar.timeInMillis
                    val endDateText = endDateEditText.text.toString()

                    if(endDateText.isEmpty()) {
                        startDateEditText.setText(sdf.format(calendar.time))
                    } else {
                        val endDate: Long = sdf.parse(endDateText)?.time!!
                        if(startDate < endDate)
                            startDateEditText.setText(sdf.format(calendar.time))
                    }
                }

            startDateEditText.setOnClickListener {
                DatePickerDialog(activity,
                    startDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            val endDateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
                    val endDate: Long = calendar.timeInMillis

                    val startDateText = startDateEditText.text.toString()
                    if(startDateText.isEmpty()) {
                        endDateEditText.setText(sdf.format(calendar.time))
                    } else {
                        val startDate: Long = sdf.parse(startDateText)?.time!!
                        if(startDate < endDate)
                            endDateEditText.setText(sdf.format(calendar.time))
                    }
                }

            endDateEditText.setOnClickListener {
                DatePickerDialog(activity,
                    endDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            filterStartButton.setOnClickListener {
                tempPost.clear()

                val titleKeyword = titleEditText.text.toString()
                val destinationKeyword = destinationEditText.text.toString()
                val startDateKeyword = startDateEditText.text.toString()
                val endDateKeyword = endDateEditText.text.toString()

                Log.d("DATE", startDateKeyword)
                Toast.makeText(activity, titleKeyword, Toast.LENGTH_LONG).show()

                val priorityList: ArrayList<Pair<UserPost, Int>> = ArrayList()

                if(titleKeyword.isEmpty() && destinationKeyword.isEmpty()) {
                    posts.forEach {priorityList.add(Pair(it, 0))}
                } else {
                    posts.forEach {
                        var votes = 0

                        if (it.Title.contains(titleKeyword) && titleKeyword.isNotEmpty()) {
                            votes++
                        }
                        if (it.Destination.contains(destinationKeyword) && destinationKeyword.isNotEmpty()) {
                            votes++
                        }

                        if(votes > 0) {
                            val currentItem = Pair(it, votes)
                            priorityList.add(currentItem)
                        }
                    }
                }


                priorityList.sortWith(compareBy {it.second})
                priorityList.reverse()

                val secondPriorityList: ArrayList<Pair<UserPost, Long>> = ArrayList()
                priorityList.forEach {
                    var prio = 0L
                    val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)

                    if (startDateKeyword.isNotEmpty() && endDateKeyword.isEmpty()) {
                        val startDateEntered: Long = sdf.parse(startDateKeyword)?.time!!
                        if(startDateEntered <= it.first.StartDate )
                            prio = it.first.StartDate
                    } else if (startDateKeyword.isEmpty() && endDateKeyword.isNotEmpty()) {
                        val endDateEntered: Long = sdf.parse(endDateKeyword)?.time!!
                        if(endDateEntered >= it.first.EndDate )
                            prio = it.first.StartDate
                    } else if (startDateKeyword.isNotEmpty() && endDateKeyword.isNotEmpty()) {
                        val startDateEntered: Long = sdf.parse(startDateKeyword)?.time!!
                        val endDateEntered: Long = sdf.parse(endDateKeyword)?.time!!
                        if(startDateEntered <= it.first.StartDate  && endDateEntered >= it.first.EndDate )
                            prio = it.first.StartDate
                    } else {
                        prio = it.second.toLong()
                    }

                    if(prio != 0L) {
                        val currentItem = Pair(it.first, prio)
                        secondPriorityList.add(currentItem)
                    }
                }

                secondPriorityList.sortWith(compareBy {it.second})
                secondPriorityList.forEach {
                    tempPost.add(it.first)
                }

                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment settings_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            all_post_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}