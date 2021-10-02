package com.bikodbg.sharetopinboard

import android.content.Context
import androidx.preference.PreferenceManager

class Preferences(context: Context) {
    val token get() = sharedPreferences.getString(tokenKey, null)

    val private get() = sharedPreferences.getBoolean(privateByDefaultKey, false)

    val unread get() = sharedPreferences.getBoolean(unreadByDefaultKey, false)

    private val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    private val tokenKey = context.getString(R.string.pinboard_token)

    private val privateByDefaultKey =
        context.getString(R.string.pinboard_private_by_default)

    private val unreadByDefaultKey =
        context.getString(R.string.pinboard_unread_by_default)
}
