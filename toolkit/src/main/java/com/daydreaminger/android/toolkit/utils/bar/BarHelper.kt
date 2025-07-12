package com.daydreaminger.android.toolkit.utils.bar

import android.content.Context

object BarHelper {

    private const val UNSET_HEIGHT = -1

    /**
     * 配合Splash来通过WindowsInsets来获取的状态栏高度
     * */
    private var statusBarHeightWithInsets = UNSET_HEIGHT

    /**
     * 配合Splash来通过WindowsInsets来获取的导航栏高度
     * */
    private var navigationBarsHeightWithInsets = UNSET_HEIGHT

    /**
     * 记录通过WindowsInsets获取的状态栏高度
     * */
    @JvmStatic
    fun updateStatusBarWithInsets(height: Int) {
        statusBarHeightWithInsets = height
    }

    /**
     * 记录通过WindowsInsets获取的导航栏高度
     * */
    @JvmStatic
    fun updateNavigationBarWithInsets(height: Int) {
        navigationBarsHeightWithInsets = height
    }

    /**
     * 获取状态栏高度（需要提前通过WindowsInsets获取）
     * */
    @JvmStatic
    fun getStatusBarHeight(): Int {
        return statusBarHeightWithInsets
    }

    /**
     * 获取导航栏高度（需要提前通过WindowsInsets获取）
     * */
    @JvmStatic
    fun getNavigationBarHeight(): Int {
        return navigationBarsHeightWithInsets
    }

    /**
     * 获取状态栏高度
     * */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        return if (statusBarHeightWithInsets != UNSET_HEIGHT) {
            statusBarHeightWithInsets
        } else {
            BarUtils.getStatusBarHeight(context)
        }
    }

    /**
     * 获取导航栏高度
     * */
    @JvmStatic
    fun getNavigationBarHeight(context: Context): Int {
        return if (navigationBarsHeightWithInsets != UNSET_HEIGHT) {
            navigationBarsHeightWithInsets
        } else {
            BarUtils.getNavigationHeight(context)
        }
    }
}