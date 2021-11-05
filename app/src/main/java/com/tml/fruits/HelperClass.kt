package com.tml.fruits

import android.app.AlertDialog
import android.content.Context
import android.widget.TextView
import org.jetbrains.anko.layoutInflater
import org.json.JSONArray
import org.json.JSONObject


class HelperClass {

    companion object {
        var pDialog: AlertDialog? = null

        // Show a loading progress dialog
        fun showLoading(ctx: Context?, pTitle: String) {
            try {
                hideProgressBar()
                if (ctx != null) {
                    val builder = AlertDialog.Builder(ctx)
                    builder.setCancelable(false)
                    val inflater = ctx.layoutInflater
                    val dialogView = inflater.inflate(R.layout.alert_dalog_loading, null)
                    builder.setView(dialogView)

                    val loadingTextView = dialogView.findViewById<TextView>(R.id.loadingTextView)
                    loadingTextView.text = pTitle

                    pDialog = builder.create()
                    pDialog!!.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Hide loading progress dialog
        fun hideProgressBar() {
            try {
                if (pDialog != null && pDialog!!.isShowing) {
                    pDialog!!.dismiss()
                    pDialog = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getJsonArrayFromJson(jsonObj: JSONObject?, key: String): JSONArray? {
            return try {
                if (jsonObj?.has(key) == true && !jsonObj.isNull(key))
                    jsonObj.getJSONArray(key)
                else
                    null
            } catch (e: Exception) {
                null
            }
        }

    }
}