package com.bikodbg.sharetopinboard

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

fun toastOnUiThread(context: Context, text: CharSequence, duration: Int) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, text, duration).show()
    }
}

fun reportError(tag: String, userMessage: String, context: Context, logMessage: String? = null) {
    toastOnUiThread(context, userMessage, Toast.LENGTH_LONG)
    Log.e(tag, logMessage ?: userMessage)
}

fun reportException(tag: String, userMessage: String, exception: Throwable, context: Context) {
    reportError(tag, userMessage, context, Log.getStackTraceString(exception))
}
