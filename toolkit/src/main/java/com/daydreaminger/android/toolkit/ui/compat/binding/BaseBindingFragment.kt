package com.daydreaminger.android.toolkit.ui.compat.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.daydreaminger.android.toolkit.ui.compat.BridgeCompatFragment
import com.daydreaminger.android.toolkit.utils.binding.BindingUtils
import com.daydreaminger.android.toolkit.utils.log.LogHelper

/**
 * 通过泛型自动解析Bingding布局实例的基类
 *
 * @Author: daydreaminger
 * @CreateDate: 2023/2/19 16:34
 */
abstract class BaseBindingFragment<V : ViewBinding> : BridgeCompatFragment() {
    protected lateinit var binding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reflectBinding =
            BindingUtils.inflateBindingWithReflect<V>(this.javaClass, layoutInflater)
        if (reflectBinding == null) {
            LogHelper.w(TAG, "onCreateView: inflate binding with reflect failure.")
            return null
        } else {
            binding = reflectBinding
            return binding.root
        }
    }

    /**
     * binding是否已经初始化了
     * */
    protected fun isInflaterForBinding(): Boolean {
        return this::binding.isInitialized
    }

    companion object {
        const val TAG = "BaseBindingFragment"
    }
}