package com.daydreaminger.android.toolkit.utils.bar

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

object BarUtils {

    /**
     * 获取顶部状态栏高度
     * */
    @JvmStatic
    fun getStatusBarHeight(context: Context?): Int {
        if (context == null) {
            return 0
        }
        var result = 0
        val resId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        result = if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                result.toFloat(), Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }

    /**
     * 获取底部导航栏高度
     * */
    @JvmStatic
    fun getNavigationHeight(context: Context?): Int {
        if (context == null) {
            return 0
        }
        val resourceId: Int = context.resources.getIdentifier(
            "navigation_bar_height", "dimen", "android"
        )
        var height = 0
        if (resourceId > 0) {
            height = context.resources.getDimensionPixelSize(resourceId)
        }
        return height
    }
}