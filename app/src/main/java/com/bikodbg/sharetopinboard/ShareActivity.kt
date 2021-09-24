package com.bikodbg.sharetopinboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

private const val TAG = "ShareActivity"

class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            if (!isPlainTextShareIntent(intent)) {
                return
            }
            Log.d(TAG, "Share intent received")

            val url = intent.getCharSequenceExtra(Intent.EXTRA_TEXT)
            val title = intent.getCharSequenceExtra(Intent.EXTRA_SUBJECT)
            val workRequest = createWorkRequest(url, title)

            WorkManager.getInstance(applicationContext).enqueue(workRequest)
            Log.d(TAG, "Work item scheduled")
        } catch (exception: Throwable) {
            reportException(TAG, "Share failed", exception, applicationContext)
        } finally {
            finish()
        }
    }

    private fun isPlainTextShareIntent(intent: Intent): Boolean {
        if (intent.action == null) {
            return false
        }

        if (intent.action != Intent.ACTION_SEND) {
            return false
        }

        if ("text/plain" != intent.type) {
            return false
        }

        return true
    }

    private fun createWorkRequest(url: CharSequence?, title: CharSequence?): OneTimeWorkRequest {
        val workerData = Data.Builder()
            .putString(PinboardWorker.URL_KEY, url?.toString())
            .putString(PinboardWorker.TITLE_KEY, title?.toString())
            .build()

        return OneTimeWorkRequestBuilder<PinboardWorker>()
            .setInputData(workerData)
            .build()
    }
}
