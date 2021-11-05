package com.tml.fruits.ui.entry

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.tml.fruits.CustomSpinnerAdapter
import com.tml.fruits.R
import com.tml.fruits.api.APIs
import com.tml.fruits.api.Services
import com.tml.fruits.model.FruitsModel
import org.json.JSONObject
import java.util.ArrayList


class EntryDetailFragment : Fragment() {

    var entryID = 0
    var selectedFruit = 0
    var fruitPositionSelected = 0
    var nameFruit = ""
    private var fruitsData = ArrayList<FruitsModel>()

    lateinit var fruitImageView: ImageView
    lateinit var dateTextView: TextView
    lateinit var fruitSpinner: Spinner
    lateinit var vitaminsEditText: EditText
    lateinit var amountEditText: EditText
    lateinit var updateButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_entry_detail, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { v, event ->
            amountEditText.clearFocus()
            true
        }

        fruitImageView = view.findViewById(R.id.fruitImageView)
        fruitSpinner = view.findViewById(R.id.fruitSpinner)
        vitaminsEditText = view.findViewById(R.id.vitaminsEditText)
        dateTextView = view.findViewById(R.id.dateTextView)
        amountEditText = view.findViewById(R.id.amountEditText)
        updateButton = view.findViewById(R.id.updateButton)

        // update send request
        updateButton.setOnClickListener {
            updateEntry()
        }

        amountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // when edit text focus changes, change the vitamin calculation
        amountEditText.setOnFocusChangeListener { view, b ->
            if (!view.isFocused) {
                val amount = amountEditText.text.toString()
                if (amount != "") {
                    vitaminsEditText.setText(getVitamins(amount.toInt(), fruitsData).toString())
                }
            } else {
                vitaminsEditText.setText("")
            }
        }

        entryID = requireArguments().getInt("entryID")
        // get entry data
        getEntryDetail(entryID)
        // getFruits data
        getFruits()
    }

    fun updateEntry() {
        val amount = amountEditText.text.toString()
        val entriesService = Services(this, requireContext())
        entriesService.updateGivenEntryService(entryID = entryID, fruit = fruitPositionSelected, amount = amount.toInt(),
            onSuccess = { response ->
                showToast("Entry updated")
            },
            onFailure = {
                showToast(it ?: "There was an error")
            }

        )
    }

    /**
     * api to get given date/entry, related details
     */
    private fun getEntryDetail(entryID: Int) {
        val entriesService = Services(this, requireContext())
        entriesService.getEntryData(entryID = entryID,
            onSuccess = { response ->
                showToast("Entries data received")
                updateUI(response)
            },
            onFailure = {
                showToast(it ?: "Failed to get entry data")
            }
        )
    }

    /**
     * update view logic
     */
    private fun updateUI(dataObject: JSONObject) {

        if (dataObject.has("date") && !dataObject.isNull("date")) {
            dateTextView.text = dataObject.getString("date")
        }

    }

    /**
     * function used in order to get the vitamins calculation
     */
    private fun getVitamins(amount: Int, fruits: List<FruitsModel>) : Int {
        var vitamins = 0
        fruits.forEach { fruit ->
            if (fruit.type.toString() == nameFruit)
            vitamins += amount * fruit.vitamins
        }
        return vitamins
    }

    /**
     * api to get given date/entry, related details
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
                setFruitSpinnerAdapter(fruitsData)
            },
            onFailure = {
                showToast(it ?: "Failed to get fruits data")
            }
        )
    }

    // fill spinner with data
    private fun setFruitSpinnerAdapter(fruits: List<FruitsModel>) {
        val fruitNames = fruits.map { it.type }
        val adapter = CustomSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, fruitNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fruitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    selectedFruit = 0
                }
                else {
                    nameFruit = fruitNames[position -1].toString()
                    fruitPositionSelected = fruitNames.indexOf(nameFruit)

                    var urlAdded = ""
                    fruitsData.forEach {
                        if (it.type == nameFruit) {
                            urlAdded = it.image ?: ""
                        }
                    }

                    // set image of the selected entry
                    Glide.with(context!!)
                        .load(APIs.BASIC_URL.value + urlAdded )
                        .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background))
                        .into(fruitImageView)

                }
            }
        }
        fruitSpinner.adapter = adapter
    }



    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}
