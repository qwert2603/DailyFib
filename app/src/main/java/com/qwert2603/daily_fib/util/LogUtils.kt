package com.qwert2603.daily_fib.util

import android.util.Log
import com.qwert2603.daily_fib.BuildConfig

object LogUtils {

    const val APP_TAG = "AASSDD"
    const val ERROR_MSG = "ERROR!!!"

    fun d(s: String) {
        d(APP_TAG, s)
    }

    fun d(tag: String, s: String) {
        if (BuildConfig.DEBUG) Log.d(tag, s)
    }

    @JvmOverloads
    fun e(s: String = ERROR_MSG, t: Throwable? = null) {
        if (BuildConfig.DEBUG) Log.e(APP_TAG, s, t)
    }
}
