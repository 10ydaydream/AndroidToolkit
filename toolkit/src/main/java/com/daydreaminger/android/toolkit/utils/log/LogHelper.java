package com.daydreaminger.android.toolkit.utils.log;

import android.util.Log;

import com.daydreaminger.android.lib.thirdparty.BuildConfig;

/**
 * Logcat输出相关工具方法
 */
public class LogHelper {
    public static final String TAG = "LogHelper";

    private static boolean sDebugEnable = BuildConfig.DEBUG;

    public static boolean isDebugEnable() {
        return sDebugEnable;
    }

    public static void setDebugEnable(boolean sDebug) {
        LogHelper.sDebugEnable = sDebug;
    }

    // bridge

    public static void v(String tag, String msg) {
        if (!sDebugEnable) {
            return;
        }
        Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (!sDebugEnable) {
            return;
        }
        Log.v(tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        if (!sDebugEnable) {
            return;
        }
        Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (!sDebugEnable) {
            return;
        }
        Log.d(tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        if (!sDebugEnable) {
            return;
        }
        Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (!sDebugEnable) {
            return;
        }
        Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        if (!sDebugEnable) {
            return;
        }
        Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (!sDebugEnable) {
            return;
        }
        Log.w(tag, msg, tr);
    }

    public static void w(String tag, Throwable tr) {
        if (!sDebugEnable) {
            return;
        }
        Log.w(tag, tr);
    }

    public static void e(String tag, String msg) {
        if (!sDebugEnable) {
            return;
        }
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (!sDebugEnable) {
            return;
        }
        Log.w(tag, msg, tr);
    }
}
