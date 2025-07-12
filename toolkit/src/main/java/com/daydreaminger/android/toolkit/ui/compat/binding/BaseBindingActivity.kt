package com.daydreaminger.androiddev.common.view.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.daydreaminger.android.toolkit.ui.compat.BridgeCompatActivity
import com.daydreaminger.android.toolkit.utils.binding.BindingUtils

/**
 *
 * @Author: daydreaminger
 * @CreateDate: 2023/2/11 9:51
 */
abstract class BaseBindingActivity<V : ViewBinding> : BridgeCompatActivity() {
    companion object {
        const val TAG = "BaseBindingActivity"
    }

    protected lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeBindingView()
        setLayoutWithReflect()
    }

    /**
     * 设置布局前的一些操作
     * */
    open fun beforeBindingView() {

    }

    /**
     * 根据翻新通过反射设置布局
     * @return 返回是否成功设置了布局
     * */
    protected open fun setLayoutWithReflect(): Boolean {
        var reflectBinding =
            BindingUtils.inflateBindingWithReflect<V>(this.javaClass, layoutInflater)
        return if (reflectBinding == null) {
            false
        } else {
            binding = reflectBinding
            setContentView(binding.root)
            true
        }
    }

    protected fun isInflaterForBinding(): Boolean {
        return this::binding.isInitialized
    }
}