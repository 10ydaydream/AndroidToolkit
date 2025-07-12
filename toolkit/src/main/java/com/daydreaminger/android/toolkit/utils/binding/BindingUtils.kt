package com.daydreaminger.android.toolkit.utils.binding

import android.view.LayoutInflater
import com.daydreaminger.android.toolkit.utils.log.LogHelper
import java.lang.reflect.ParameterizedType

/**
 *
 * @Author: daydreaminger
 * @CreateDate: 2023/2/19 17:55
 */
object BindingUtils {
    const val TAG = "BindingUtils"

    /**
     * ViewBinding接口的全路径名称
     * */
    private const val BINDING_INTERFACE_PKG_PATH = "androidx.viewbinding.ViewBinding"

    /**
     * 反射获取
     * @param hostClass 对应实例，用于反射获取泛型中是否有对应ViewBinding或DataBinding实现
     * @param layoutInflater LayoutInflater实例，用于生成ViewBinging/DataBinding实例
     * @param index 泛型位置，默认为第一个，如果有必要根据实际泛型参数指定对应下标位置，下标从0开始
     * @return 返回泛型中Binding实例，没有则返回null
     * @throws IllegalArgumentException 传入的带泛型的Class中，指定下标的泛型必须为ViewBinging/DataBinding子类，否则抛出异常
     * */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <V> inflateBindingWithReflect(
        hostClass: Class<*>,
        layoutInflater: LayoutInflater,
        index: Int = 0
    ): V? {
        // 检查是否有依赖ViewBinding或者DataBinding
        try {
            Class.forName(BINDING_INTERFACE_PKG_PATH) ?: return null
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalArgumentException("arg class not implement interface ViewBinding or DataBinding.")
        }
        // 读取Class中注解的相关信息，这里拿的就是类定义的泛型类 -- 因为泛型特性，编译后子类的泛型信息会记录对应的类型
        val parameterizedType = hostClass.genericSuperclass as ParameterizedType
        // 读取其中泛型列表的实际类型Class对象列表
        val actualTypeArgs = parameterizedType.actualTypeArguments
        // 尝试反射处理生成泛型的Binding实例
        if (actualTypeArgs.isNotEmpty()) {
            return try {
                // 拿到泛型类型的Class对象，然后强制转换
                // 因为actualTypeArgs[0]拿到的是Class实例，而且现在也没有泛型的实例，所以无法直接和泛型V进行比较
                val bindingClass: Class<V> = actualTypeArgs[index] as Class<V>
                // 使用具体Binding的静态方法：XXXBinding.inflate(layoutInflater)方法
                // 从源码上看，xml布局生成的Binding类，都会带有这个静态方法，用于创建Binding类实例
                // ViewBinding和DataBinding都可以通过这个方法来创建对应的实例
                val method = bindingClass.getMethod("inflate", LayoutInflater::class.java)
                val dataBinding = method.invoke(null, layoutInflater)
                // 返回强转后的具体Binding类型的实例
                dataBinding as V
            } catch (e: Exception) {
                LogHelper.e(TAG, "inflateBindingWithReflect: error.", e)
                null
            }
        }
        return null
    }
}