package com.tml.fruits.ui.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tml.fruits.HelperClass
import com.tml.fruits.R
import com.tml.fruits.api.Services
import com.tml.fruits.model.FruitModel
import org.json.JSONArray
import java.util.ArrayList

/**
 * Created By Elio Lako
 */
class EntriesFragment : Fragment() {

    lateinit var editButton: Button
    lateinit var noEntriesAvailable: TextView
    lateinit var enteredEntries: RecyclerView

    private lateinit var entriesEnteredAdapter: EnteriesEnteredAdapter
    private var enteredEntriesData = ArrayList<FruitModel>()

    var entryID = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enteredEntries = view.findViewById(R.id.enteredEntries)
        noEntriesAvailable = view.findViewById(R.id.noEntriesAvailable)
        enteredEntries.layoutManager = LinearLayoutManager(context)

        editButton = view.findViewById(R.id.editButton)
        // on click for editButton to open fragment and fill data
        editButton.setOnClickListener {
            val bundle = bundleOf("entryID" to entryID)
            findNavController().navigate(R.id.navigation_entry, bundle)
        }

        entryID = requireArguments().getInt("entryID")
        // get entry data
        getEntryDetail(entryID)

    }

    private fun getEntryDetail(entryID: Int) {
        val entriesService = Services(this, requireContext())
        entriesService.getEntryData(entryID = entryID,
            onSuccess = { response ->
                try {
                    val fruitsArray = HelperClass.getJsonArrayFromJson(response, "fruit") ?: JSONArray()
                    if (fruitsArray.length() > 0) {
                        showToast("Entries data received")

                        for (i in 0 until fruitsArray.length()) {
                            val entriesJsonObj = fruitsArray.getJSONObject(i)
                            val entries = Gson().fromJson(entriesJsonObj.toString(), FruitModel::class.java)
                            enteredEntries.visibility = View.VISIBLE
                            enteredEntriesData.add(entries)
                            entriesEnteredAdapter = EnteriesEnteredAdapter(enteredEntriesData)
                            enteredEntries.adapter = entriesEnteredAdapter
                        }
                    } else {
                        noEntriesAvailable.visibility = View.VISIBLE
                        enteredEntries.visibility = View.GONE
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
                        },
            onFailure = {
                enteredEntries.visibility = View.GONE
                noEntriesAvailable.visibility = View.VISIBLE
                showToast(it ?: "Failed to get entry data")
            }
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}
