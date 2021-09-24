package com.bikodbg.sharetopinboard

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "PinboardWorker"

class PinboardWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        const val URL_KEY = "url"
        const val TITLE_KEY = "title"
    }

    override fun doWork(): Result {
        try {
            val title = inputData.getString(TITLE_KEY) ?: "[no title]"
            Log.i(TAG, "Title: $title")

            val url = inputData.getString(URL_KEY)
            if (null == url) {
                reportError(TAG, "No URL shared", applicationContext)
                return Result.failure()
            }
            Log.i(TAG, "URL: $url")

            val preferences = Preferences(applicationContext)

            val token = preferences.token
            if (null == token) {
                reportError(TAG, "No Pinboard token specified", applicationContext)
                return Result.failure()
            }

            val pinboard = Pinboard(token)

            try {
                pinboard.add(
                    url,
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
}
