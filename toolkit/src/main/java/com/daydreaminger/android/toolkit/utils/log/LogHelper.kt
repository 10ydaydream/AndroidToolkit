package com.daydreaminger.android.toolkit.utils.log

import android.util.Log
import com.daydreaminger.android.toolkit.BuildConfig

/**
 * Logcat输出相关工具方法
 */
object LogHelper {
    const val TAG: String = "LogHelper"

    var isDebugEnable: Boolean = BuildConfig.DEBUG

    // ===== bridge Log class function.

    @JvmStatic
    fun v(tag: String?, msg: String) {
        if (!isDebugEnable) {
            return
        }
        Log.v(tag, msg)
    }

    @JvmStatic
    fun v(tag: String?, msg: String?, tr: Throwable?) {
        if (!isDebugEnable) {
            return
        }
        Log.v(tag, msg, tr)
    }

    @JvmStatic
    fun d(tag: String?, msg: String) {
        if (!isDebugEnable) {
            return
        }
        Log.d(tag, msg)
    }

    @JvmStatic
    fun d(tag: String?, msg: String?, tr: Throwable?) {
        if (!isDebugEnable) {
            return
        }
        Log.d(tag, msg, tr)
    }

    @JvmStatic
    fun i(tag: String?, msg: String) {
        if (!isDebugEnable) {
            return
        }
        Log.i(tag, msg)
    }

    @JvmStatic
    fun i(tag: String?, msg: String?, tr: Throwable?) {
        if (!isDebugEnable) {
            return
        }
        Log.i(tag, msg, tr)
    }

    @JvmStatic
    fun w(tag: String?, msg: String) {
        if (!isDebugEnable) {
            return
        }
        Log.w(tag, msg)
    }

    @JvmStatic
    fun w(tag: String?, msg: String?, tr: Throwable?) {
        if (!isDebugEnable) {
            return
        }
        Log.w(tag, msg, tr)
    }

    @JvmStatic
    fun w(tag: String?, tr: Throwable?) {
        if (!isDebugEnable) {
            return
        }
        Log.w(tag, tr)
    }

    @JvmStatic
    fun e(tag: String?, msg: String) {
        if (!isDebugEnable) {
            return
        }
        Log.w(tag, msg)
    }

    @JvmStatic
    fun e(tag: String?, msg: String?, tr: Throwable?) {
        if (!isDebugEnable) {
            return
        }
        Log.w(tag, msg, tr)
    }
}
