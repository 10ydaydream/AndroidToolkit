package com.daydreaminger.androiddev.common.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.daydreaminger.android.toolkit.ui.compat.BridgeCompatFragment
import com.daydreaminger.android.toolkit.utils.binding.BindingUtils

/**
 *
 * @Author: daydreaminger
 * @CreateDate: 2023/2/19 16:34
 */
abstract class BaseBindingFragment<V : ViewBinding> : BridgeCompatFragment() {
    companion object {
        const val TAG = "BaseBindingFragment"
    }

    protected lateinit var binding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reflectBinding =
            BindingUtils.inflateBindingWithReflect<V>(this.javaClass, layoutInflater)
        if (reflectBinding == null) {
            return TextView(context)
        } else {
            binding = reflectBinding
            return binding.root
        }
    }
}