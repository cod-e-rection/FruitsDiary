package com.tml.fruits.ui.home

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.tml.fruits.HelperClass
import com.tml.fruits.R
import com.tml.fruits.api.Services
import com.tml.fruits.model.EntriesModel
import com.tml.fruits.model.FruitsModel
import java.text.SimpleDateFormat
import java.util.*


class HomeEntriesFragment : Fragment(), OnInteractionEntryInterface {

    private var entriesRecyclerView : RecyclerView? = null
    private var fabActionButton: FloatingActionButton? = null

    private lateinit var entriesAdapter: EntriesRecyclerViewAdapter
    private var entriesData = ArrayList<EntriesModel>()
    private var fruitsData = ArrayList<FruitsModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        entriesRecyclerView = view.findViewById(R.id.entriesRecyclerView)
        entriesRecyclerView?.layoutManager = LinearLayoutManager(context)

        fabActionButton = view.findViewById(R.id.floatingActionButton)
        fabActionButton?.setOnClickListener { fab: View? ->
            val myCalendar = Calendar.getInstance()
            val onDateSetListener = OnDateSetListener { view1: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                myCalendar[year, monthOfYear] = dayOfMonth
                // execute API to add entry
                addEntry(myCalendar, SimpleDateFormat("yyyy-MM-dd"))
            }
            DatePickerDialog(requireContext(), onDateSetListener,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]).show()
        }

        getFruits()
        getEntriesService()
    }

    /**
     * api to get given fruits
     */
    private fun getFruits() {
        val fruitsService = Services(this, requireContext())
        fruitsService.getGivenFruitsData(
            onSuccess = { jsonArray ->
                for (i in 0 until jsonArray.length()) {
                    val fruitJsonObj = jsonArray.getJSONObject(i)
                    val fruit = Gson().fromJson(fruitJsonObj.toString(), FruitsModel::class.java)
                    fruitsData.add(fruit)
                }
            },
            onFailure = {
                showToast(it ?: "Failed to get fruits data")
            }
        )
    }

    /**
     * Service used in order to get entries by Date
     */
    private fun getEntriesService() {
        val entriesService = Services(this, requireContext())
        entriesService.getEntriesService(
            onSuccess = { jsonArray ->
                if (jsonArray.length() == 0) {
                    Toast.makeText(activity, "No entries available", LENGTH_LONG).show()
                } else {
                    HelperClass.hideProgressBar()
                    for (i in 0 until jsonArray.length()) {
                        val entriesJsonObj = jsonArray.getJSONObject(i)
                        val entries =  Gson().fromJson(entriesJsonObj.toString(), EntriesModel::class.java)
                        entriesData.add(entries)
                        // sorting List by date descending way
                        entriesData.sortByDescending { it.date }
                        entriesAdapter = EntriesRecyclerViewAdapter(entriesData, fruitsData)
                        entriesAdapter.onInteractionEntryInterface = this
                        entriesRecyclerView?.adapter = entriesAdapter
                        entriesAdapter.notifyDataSetChanged()
                    }
                }
            },
            onFailure = {
                showToast(it ?: "")
            }
        )
    }

    override fun onPause() {
        super.onPause()
        entriesData.clear()
    }

    private fun addEntry(myCalendar: Calendar, format: SimpleDateFormat) {
        val entriesService = Services(this, requireContext())
        entriesService.addEntry(myCalendar = myCalendar, format = format,
            onSuccess = { response ->
                showToast("Entries updated")
                entriesData.clear()
                getEntriesService()
            },
            onFailure = {
                showToast("Failed to add Entry. Possible duplicate?")
            }
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_LONG).show()
    }

    /**
     * Override func used in order to execute API and delete the given entry
     */
    override fun removeEntry(entryID: Int?) {
        if (entryID != null) {
            val entriesService = Services(this, requireContext())
            entriesService.deleteEntryService(entryID = entryID,
                onSuccess = { json ->
                    showToast("Entry Removed")
                    entriesData.clear()
                    getEntriesService()
                },
                onFailure = {
                    showToast(it ?: "")
                }
            )
        } else {
            showToast("Something is wrong")
        }
    }

    /**
     * Override func used for onClick to open entry details view
     */
    override fun onClickEntry(entryID: Int?) {
        if (entryID != null) {
            val bundle = bundleOf("entryID" to entryID)
            findNavController().navigate(R.id.navigation_entered_entries, bundle)
        } else {
            showToast("Something is wrong")
        }
    }

}
