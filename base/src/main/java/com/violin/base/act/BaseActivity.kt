package com.violin.base.act

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("ViewBinding accessed before onCreate or after onDestroy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1️⃣ 创建 ViewBinding
        _binding = createBinding(layoutInflater)

        // 2️⃣ 设置根布局
        setContentView(binding.root)

        // 3️⃣ 统一初始化流程
        initView()
        initData()
        initObserver()
    }

    /**
     * 子类必须实现：创建自己的 ViewBinding
     */
    protected abstract fun createBinding(inflater: LayoutInflater): VB

    /**
     * 初始化 View
     */
    protected open fun initView() {}

    /**
     * 初始化数据
     */
    protected open fun initData() {}

    /**
     * 初始化观察者（可选，用于 LiveData / Flow）
     */
    protected open fun initObserver() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}