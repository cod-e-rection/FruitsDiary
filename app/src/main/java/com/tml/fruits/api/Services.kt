package com.tml.fruits.api

import android.content.Context
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.tml.fruits.HelperClass
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Services(activity: Fragment, var context: Context?) : BaseService(activity) {

    /**
     * Api to get Entries request data
     * @param onSuccess function to execute when API call success.
     * @param onFailure function to execute when API call failure.
     */
    fun getEntriesService(onSuccess: (JSONArray) -> Unit, onFailure: (String?) -> Unit) {

        val queue = Volley.newRequestQueue(context)
        val url = APIs.CURRENT_ENTRIES.value
        HelperClass.showLoading(context, "Loading...")

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null, { jsonArray ->
            try {
                HelperClass.hideProgressBar()
                onSuccess(jsonArray)
            } catch (e: JSONException) {
                onFailure(e.toString())
                e.printStackTrace()
                HelperClass.hideProgressBar()
            }
        }) {
            it.printStackTrace()
            onFailure(it.toString())
        }
        queue.add(jsonArrayRequest)
    }

    /**
     * Service used in delete a entry
     * @param entryID
     * @param onSuccess function to execute when API call success.
     * @param onFailure function to execute when API call failure.
     */
    fun deleteEntryService(entryID: Int?, onSuccess: (JSONObject) -> Unit, onFailure: (String?) -> Unit) {

        val queue = Volley.newRequestQueue(context)
        val url = APIs.DELETE_CURRENT_ENTRY.value + "/$entryID"
        HelperClass.showLoading(context, "Deleting Entry...")

        val jsonParams = JSONObject()

        val request = JsonRequest(context!!, Request.Method.DELETE, url, jsonParams, { jsonObj ->
            try {
                HelperClass.hideProgressBar()
                onSuccess(jsonObj)
            } catch (e: JSONException) {
                onFailure(e.toString())
                e.printStackTrace()
                HelperClass.hideProgressBar()
            }
        }) {
            HelperClass.hideProgressBar()
            it.printStackTrace()
            onFailure(it.toString())
        }
        queue.add(request)
    }

    /**
     * Service used in order to add a new entry ID (date)
     * @param myCalendar date selected
     * @param format desired server formatted date
     * @param onSuccess function to execute when API call success.
     * @param onFailure function to execute when API call failure.
     */

    fun addEntry(myCalendar: Calendar, format: SimpleDateFormat,
                 onSuccess: (JSONObject) -> Unit,
                 onFailure: (String?) -> Unit) {

        val timeStamped = format.format(myCalendar.time)
        // Setup the request queue and API to call
        val queue = Volley.newRequestQueue(context)
        val url = APIs.CURRENT_ENTRIES.value
        HelperClass.showLoading(context, "Adding Entry...")

        val jsonParams = JSONObject()

        jsonParams.put("date", timeStamped)
        // Request response from the server
        val request = JsonRequest(context!!, Request.Method.POST, url, jsonParams, { response ->
            try {
                println(response)
                HelperClass.hideProgressBar()
                onSuccess(response)
            } catch (e: Exception) {
                HelperClass.hideProgressBar()
                e.printStackTrace()
            } }, { e ->
                e.printStackTrace()
                onFailure(e.toString())
                HelperClass.hideProgressBar()
        })
        // Set the retry policy and add the request to the queue to send it
        request.retryPolicy = DefaultRetryPolicy(5000, 0, 2.toFloat())
        queue.add(request)
    }

    /**
     * Service used in order to get entry data
     * @param entryID
     * @param onSuccess function to execute when API call success.
     * @param onFailure function to execute when API call failure.
     */
    fun getEntryData(entryID: Int?, onSuccess: (JSONObject) -> Unit, onFailure: (String?) -> Unit) {

        val queue = Volley.newRequestQueue(context)
        val url = APIs.DELETE_CURRENT_ENTRY.value + "/$entryID"
        HelperClass.showLoading(context, "Getting Entry details...")

        val jsonParams = JSONObject()

        val request = JsonRequest(context!!, Request.Method.GET, url, jsonParams, { jsonObj ->
            try {
                HelperClass.hideProgressBar()
                onSuccess(jsonObj)
            } catch (e: JSONException) {
                onFailure(e.toString())
                e.printStackTrace()
                HelperClass.hideProgressBar()
            }
        }) {
            HelperClass.hideProgressBar()
            it.printStackTrace()
            onFailure(it.toString())
        }
        queue.add(request)
    }

    /**
     * Service used in order to update Fruits data
     * @param entryID id of the given entry
     * @param fruit fruit ID
     * @param amount number of the given fruits
     * @param onSuccess function to execute when API call success.
     * @param onFailure function to execute when API call failure.
     */
    fun updateGivenEntryService(entryID: Int?, fruit: Int?, amount: Int?,
        onSuccess: (JSONObject) -> Unit, onFailure: (String?) -> Unit) {

        val queue = Volley.newRequestQueue(context)
        val url = "https://fruitdiary.test.themobilelife.com/api/entry/$entryID/fruit/$fruit?amount=$amount"
        HelperClass.showLoading(context, "Getting Entry details...")

        val jsonParams = JSONObject()

        val request = JsonRequest(context!!, Request.Method.POST, url, jsonParams, { jsonObj ->
            try {
                HelperClass.hideProgressBar()
                onSuccess(jsonObj)
            } catch (e: JSONException) {
                onFailure(e.toString())
                e.printStackTrace()
                HelperClass.hideProgressBar()
            }
        }) {
            HelperClass.hideProgressBar()
            it.printStackTrace()
            onFailure(it.toString())
        }
        queue.add(request)
    }

    /**
     * Service used in order get Fruits data
     * @param onSuccess function to execute when API call success.
     * @param onFailure function to execute when API call failure.
     */
    fun getGivenFruitsData(onSuccess: (JSONArray) -> Unit, onFailure: (String?) -> Unit) {
        val queue = Volley.newRequestQueue(context)
        val url = APIs.FRUITS_LIST.value

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null, { jsonArray ->
            try {
                if (jsonArray.length() == 0) {
                    onFailure("No data available")
                } else {
                    onSuccess(jsonArray)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                onFailure(e.toString())
            }
        }) {
            onFailure(it.toString())
        }
        queue.add(jsonArrayRequest)
    }

}
