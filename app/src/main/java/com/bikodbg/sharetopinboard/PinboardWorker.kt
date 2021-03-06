package com.bikodbg.sharetopinboard

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bikodbg.sharetopinboard.filters.TwitterFilter

class PinboardWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val preferences = Preferences(context)

    override fun doWork(): Result {
        try {
            val title = inputData.getString(KEY_TITLE) ?: "[no title]"
            Log.i(TAG, "Title: $title")

            val url = inputData.getString(KEY_URL)
            if (null == url) {
                reportError(TAG, "No URL shared", applicationContext)
                return Result.failure()
            }
            Log.i(TAG, "URL: $url")

            val filteredUrl = applyFilters(url)
            Log.i(TAG, "Filtered URL: $filteredUrl")

            val token = preferences.token
            if (null == token) {
                reportError(TAG, "No Pinboard token specified", applicationContext)
                return Result.failure()
            }

            val pinboard = Pinboard(token)

            try {
                pinboard.add(
                    filteredUrl,
                    title,
                    replace = false,
                    shared = !preferences.private,
                    toRead = preferences.unread
                )
            } catch (exception: PinboardException) {
                reportError(TAG, exception.message!!, applicationContext)
                return Result.failure()
            }

            toastOnUiThread(applicationContext, "Added!", Toast.LENGTH_LONG)
            return Result.success()
        } catch (exception: Throwable) {
            reportException(TAG, "Upload to Pinboard failed", exception, applicationContext)
            return Result.failure()
        }
    }

    private fun applyFilters(url: String): String {
        var uri = Uri.parse(url)

        if (preferences.filterTwitter) {
            uri = TwitterFilter().apply(uri)
        }

        return uri.toString()
    }

    companion object {
        private const val TAG = "PinboardWorker"

        const val KEY_URL = "url"
        const val KEY_TITLE = "title"
    }
}
