package com.violin.base.act

import android.util.Log

object LogUtil {
    fun logD(tag: String, messageString: String) {
        Log.d("violin:$tag", messageString)
    }
    @JvmStatic
    fun d(tag: String, messageString: String) {
        Log.d("violin:$tag", messageString)
    }

    @JvmStatic
    fun i(tag: String, messageString: String) {
        Log.i("violin:$tag", messageString)
    }
    @JvmStatic
    fun w(tag: String, messageString: String) {
        Log.w("violin:$tag", messageString)
    }
    @JvmStatic
    fun e(tag: String, messageString: String) {
        Log.e("violin:$tag", messageString)
    }

    @JvmStatic
    fun v(tag: String, messageString: String) {
        Log.v("violin:$tag", messageString)
    }
}