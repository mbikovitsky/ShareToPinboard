package com.bikodbg.sharetopinboard.filters

import android.net.Uri

fun interface IURIFilter {
    fun apply(uri: Uri): Uri
}
