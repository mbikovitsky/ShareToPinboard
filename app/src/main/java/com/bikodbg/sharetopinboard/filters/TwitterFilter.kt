package com.bikodbg.sharetopinboard.filters

import android.net.Uri

class TwitterFilter : IURIFilter {
    override fun apply(uri: Uri): Uri {
        if (uri.isOpaque || uri.authority != "twitter.com") {
            return uri
        }

        return uri.buildUpon()
            .clearQuery()
            .fragment(null)
            .build()
    }
}
