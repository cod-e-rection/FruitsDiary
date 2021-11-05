package com.tml.fruits.api

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.tml.fruits.BuildConfig
import org.json.JSONObject
import java.util.HashMap


open class JsonRequest : JsonObjectRequest {

    private var mParams: JSONObject? = null
    private var mCtx: Context? = null

    constructor(ctx: Context, method: Int, url: String, params: JSONObject, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener):
            super(method, url, params, listener, errorListener)
    {
        mCtx = ctx
        mParams = params
    }

    override fun deliverResponse(response: JSONObject) {
        super.deliverResponse(response)
        try {
            val jsonObj = response
            if (jsonObj.has("code") && jsonObj.getInt("code") == 405) {

            }
        }
        catch (e: Exception) {
            // We might get JSONArray when 200
        }
    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        super.getHeaders()
        if (BuildConfig.DEBUG) {
            if (method == 0) {
                var logMsg = "--> Get $url"
                logMsg += "\n--> END"
                Log.v("ApiRequestLog", logMsg)
            }
        }
        val params = HashMap<String, String>()
        params["Content-Type"] = "application/json; charset=utf-8"
        return params
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray? {
        val bodyArray = super.getBody()
        if (BuildConfig.DEBUG) {
            var methodName = ""
            when (method) {
                0 -> methodName = "GET"
                1 -> methodName = "POST"
                2 -> methodName = "PUT"
                3 -> methodName = "DELETE"
                4 -> methodName = "HEAD"
                5 -> methodName = "OPTIONS"
                6 -> methodName = "TRACE"
                7 -> methodName = "PATCH"
            }
            var logMsg = "--> $methodName $url"
            var bodyStr = bodyArray.toString(Charsets.UTF_8)
            logMsg += "\n$bodyStr"
            logMsg += "\n--> END"
            Log.v("ApiRequestLog", logMsg)
        }
        return bodyArray
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        val networkResponse = super.parseNetworkResponse(response)
        if (BuildConfig.DEBUG) {
            val statusCode = response.statusCode
            val connection = response.headers["Connection"]
            val contentType = response.headers["Content-Type"]
            val contentLength = response.headers["Content-Length"]
            var logMsg = "<-- $statusCode $url\nConnection: $connection, Content-Type: $contentType, Content-Length: $contentLength"
            logMsg += if (networkResponse.isSuccess)
                "\n" + networkResponse.result.toString()
            else
                "\n" + networkResponse.error.toString()
            logMsg += "\n<-- END"
            Log.v("ApiRequestLog", logMsg)
        }
        return networkResponse
    }
}
