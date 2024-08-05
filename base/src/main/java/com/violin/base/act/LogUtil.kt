package com.violin.base.act

import android.util.Log

object LogUtil {
    fun logD(tag: String, messageString: String) {
        Log.d("violin:$tag", messageString)
    }
}