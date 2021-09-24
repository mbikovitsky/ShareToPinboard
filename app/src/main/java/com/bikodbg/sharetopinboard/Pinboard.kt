package com.bikodbg.sharetopinboard

import android.net.Uri
import android.util.Log
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class PinboardException(message: String) : Exception(message)

class Pinboard(private val token: String) {
    fun add(
        url: String,
        title: String,
        extended: String? = null,
        replace: Boolean = true,
        shared: Boolean = true,
        toRead: Boolean = false,
    ) {
        val builder = getBaseBuilder()
            .appendPath("posts")
            .appendPath("add")
            .appendQueryParameter("url", url)
            .appendQueryParameter("description", title)
            .appendQueryParameter("replace", pinboardBoolean(replace))
            .appendQueryParameter("shared", pinboardBoolean(shared))
            .appendQueryParameter("toread", pinboardBoolean(toRead))

        if (null != extended) {
            builder.appendQueryParameter("extended", extended)
        }

        val requestUrl = builder.build().toString()
        Log.d(TAG, requestUrl)

        val response = sendRequest(requestUrl)
        Log.d(TAG, "$response")

        val result = response["result_code"] as String
        if ("done" != result) {
            Log.e(TAG, result)
            throw PinboardException(result)
        }
    }

    private fun getBaseBuilder() = Uri.Builder()
        .scheme("https")
        .authority("api.pinboard.in")
        .appendPath("v1")
        .appendQueryParameter("auth_token", token)
        .appendQueryParameter("format", "json")

    private fun pinboardBoolean(value: Boolean) = if (value) "yes" else "no"

    private fun sendRequest(requestUrl: String): JSONObject {
        val connection = URL(requestUrl).openConnection() as HttpsURLConnection
        connection.connect()

        val response = connection.inputStream
            .readBytes()
            .toString(Charsets.UTF_8)

        return JSONObject(response)
    }

    companion object {
        private const val TAG = "Pinboard"
    }
}
