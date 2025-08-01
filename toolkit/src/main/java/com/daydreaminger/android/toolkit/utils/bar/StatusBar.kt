@file:Suppress("DEPRECATION")

package com.daydreaminger.android.toolkit.utils.bar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity

// from：https://github.com/liangjingkanji/StatusBar

/*
* 常用操作：
* 1. 修改状态栏、导航栏背景颜色
* 2. 修改状态栏、导航栏图标颜色
* 3. 设置为沉浸式状态栏（内容延伸到状态栏和导航栏）
* */

private const val COLOR_TRANSPARENT = 0

/** 设置状态栏颜色 */
@SuppressLint("ObsoleteSdkInt")
fun Activity.statusBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = color
    }
}

/** 设置状态栏颜色 */
fun Activity.statusBarColorRes(@ColorRes colorRes: Int) =
    statusBarColor(resources.getColor(colorRes))

/**
 * 使用视图的背景色作为状态栏颜色
 * @param v 提取该View的背景颜色设置为状态栏颜色, 如果该View没有背景颜色则该函数调用无效
 * @param darkMode 是否显示暗色状态栏文字颜色
 */
@JvmOverloads
fun Activity.immersive(v: View, darkMode: Boolean? = null) {
    val background = v.background
    if (background is ColorDrawable) {
        immersive(background.color, darkMode)
    }
}

/**
 * 设置透明状态栏或者状态栏颜色, 此函数会导致状态栏覆盖界面,
 * 如果不希望被状态栏遮挡Toolbar请再调用[statusPadding]设置视图的paddingTop 或者 [statusMargin]设置视图的marginTop为状态栏高度
 *
 * 如果不指定状态栏颜色则会应用透明状态栏(全屏属性), 会导致键盘遮挡输入框
 *
 * @param color 状态栏颜色, 不指定则为透明状态栏
 * @param darkMode 是否显示暗色状态栏文字颜色
 */
@SuppressLint("ObsoleteSdkInt")
@JvmOverloads
fun Activity.immersive(@ColorInt color: Int = COLOR_TRANSPARENT, darkMode: Boolean? = null) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            when (color) {
                COLOR_TRANSPARENT -> {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.decorView.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                    window.statusBarColor = color
                }

                else -> {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = color
                }
            }
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (color != COLOR_TRANSPARENT) {
                setTranslucentView(window.decorView as ViewGroup, color)
            }
        }
    }
    if (darkMode != null) {
        darkMode(darkMode)
    }
}

/**
 * 退出沉浸式状态栏并恢复默认状态栏颜色
 *
 * @param black 是否显示黑色状态栏白色文字(不恢复状态栏颜色)
 */
@SuppressLint("ObsoleteSdkInt", "UseKtx")
@JvmOverloads
fun Activity.immersiveExit(black: Boolean = false) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        // 恢复默认状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (black) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            } else {
                val typedArray = obtainStyledAttributes(intArrayOf(android.R.attr.statusBarColor))
                window.statusBarColor = typedArray.getColor(0, 0)
                typedArray.recycle()
            }
        }
    }
}

/**
 * 获取颜色资源值来设置状态栏
 */
@JvmOverloads
fun Activity.immersiveRes(@ColorRes color: Int, darkMode: Boolean? = null) =
    immersive(resources.getColor(color), darkMode)

/**
 * 开关状态栏暗色模式, 并不会透明状态栏, 只是单纯的状态栏文字变暗色调.
 *
 * @param darkMode 状态栏文字是否为暗色
 */
@JvmOverloads
fun Activity.darkMode(darkMode: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (darkMode) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }
}

/**
 * 增加View的paddingTop, 增加高度为状态栏高度, 用于防止视图和状态栏重叠
 * 如果是RelativeLayout设置padding值会导致centerInParent等属性无法正常显示
 * @param remove 如果默认paddingTop大于状态栏高度则添加无效, 如果小于状态栏高度则无法删除
 */
@SuppressLint("ObsoleteSdkInt")
@JvmOverloads
fun View.statusPadding(remove: Boolean = false) {
    if (this is RelativeLayout) {
        throw UnsupportedOperationException("Unsupported set statusPadding for RelativeLayout")
    }
    if (Build.VERSION.SDK_INT >= 19) {
        val statusBarHeight = context.statusBarHeight
        val lp = layoutParams
        if (lp != null && lp.height > 0) {
            lp.height += statusBarHeight //增高
        }
        if (remove) {
            if (paddingTop < statusBarHeight) return
            setPadding(
                paddingLeft, paddingTop - statusBarHeight,
                paddingRight, paddingBottom
            )
        } else {
            if (paddingTop >= statusBarHeight) return
            setPadding(
                paddingLeft, paddingTop + statusBarHeight,
                paddingRight, paddingBottom
            )
        }
    }
}

/**
 * 增加View的marginTop值, 增加高度为状态栏高度, 用于防止视图和状态栏重叠
 * @param remove 如果默认marginTop大于状态栏高度则添加无效, 如果小于状态栏高度则无法删除
 */
@SuppressLint("ObsoleteSdkInt")
@JvmOverloads
fun View.statusMargin(remove: Boolean = false) {
    if (Build.VERSION.SDK_INT >= 19) {
        val statusBarHeight = context.statusBarHeight
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        if (remove) {
            if (lp.topMargin < statusBarHeight) return
            lp.topMargin -= statusBarHeight
            layoutParams = lp
        } else {
            if (lp.topMargin >= statusBarHeight) return
            lp.topMargin += statusBarHeight
            layoutParams = lp
        }
    }
}

/**
 * 创建假的透明栏
 */
@SuppressLint("ObsoleteSdkInt")
private fun Context.setTranslucentView(container: ViewGroup, color: Int) {
    if (Build.VERSION.SDK_INT >= 19) {
        var simulateStatusBar: View? = container.findViewById(android.R.id.custom)
        if (simulateStatusBar == null && color != 0) {
            simulateStatusBar = View(container.context)
            simulateStatusBar.id = android.R.id.custom
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
            container.addView(simulateStatusBar, lp)
        }
        simulateStatusBar?.setBackgroundColor(color)
    }
}

/**
 * 设置ActionBar的背景颜色
 */
@SuppressLint("UseKtx")
fun AppCompatActivity.setActionBarBackground(@ColorInt color: Int) {
    supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
}

@SuppressLint("UseKtx")
fun AppCompatActivity.setActionBarBackgroundRes(@ColorRes color: Int) {
    supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(color)))
}

/**
 * 设置ActionBar的背景颜色为透明
 */
@SuppressLint("UseKtx")
fun AppCompatActivity.setActionBarTransparent() {
    supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

/**
 * 显示或隐藏导航栏, 系统开启可以隐藏, 系统未开启不能开启
 *
 * @param enabled 是否显示导航栏
 */
@SuppressLint("ObsoleteSdkInt")
@JvmOverloads
fun Activity.setNavigationBar(enabled: Boolean = true) {
    if (Build.VERSION.SDK_INT in 12..18) {
        if (enabled) {
            window.decorView.systemUiVisibility = View.VISIBLE
        } else {
            window.decorView.systemUiVisibility = View.GONE
        }
    } else if (Build.VERSION.SDK_INT >= 19) {
        val systemUiVisibility = window.decorView.systemUiVisibility
        if (enabled) {
            window.decorView.systemUiVisibility =
                // systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // 提示简化为0
                0
        } else {
            window.decorView.systemUiVisibility = systemUiVisibility or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}

/**
 * 设置是否全屏
 *
 * @param enabled 是否全屏显示
 */
@JvmOverloads
fun Activity.setFullscreen(enabled: Boolean = true) {
    val systemUiVisibility = window.decorView.systemUiVisibility
    window.decorView.systemUiVisibility = if (enabled) {
        systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    } else {
        systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
    }
}

/**
 * 是否有导航栏
 */
val Activity?.isNavigationBar: Boolean
    get() {
        this ?: return false
        val vp = window.decorView as? ViewGroup
        if (vp != null) {
            for (i in 0 until vp.childCount) {
                vp.getChildAt(i).context.packageName
                if (vp.getChildAt(i).id != -1 && "navigationBarBackground" ==
                    resources.getResourceEntryName(vp.getChildAt(i).id)
                ) return true
            }
        }
        return false
    }

/**
 * 如果当前设备存在导航栏返回导航栏高度, 否则0
 */
val Context?.navigationBarHeight: Int
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    get() {
        this ?: return 0
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        var height = 0
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId)
        }
        return height
    }


/**
 * 获取状态栏高度
 */
val Context?.statusBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() {
        this ?: return 0
        var result = 24
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                result.toFloat(), Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }

/**
 * 获取Activity的content布局
 *
 * 类型为：androidx.appcompat.widget.ContentFrameLayout
 * */
fun Activity.findContentView(): ViewGroup {
    return window.decorView.findViewById(android.R.id.content)
}

/**
 * 获取Activity设置的布局
 * */
fun Activity.findSetContentRootView(): View {
    val contentView = window.decorView.findViewById<ViewGroup>(android.R.id.content)
    return contentView.getChildAt(0)
}