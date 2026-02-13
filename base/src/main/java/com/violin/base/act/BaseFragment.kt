package com.violin.base.act

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("ViewBinding is null. View may have been destroyed.")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
        initObserver()
    }

    /**
     * 子类必须实现：创建自己的 ViewBinding
     */
    protected abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    /**
     * 初始化 View
     */
    protected open fun initView() {}

    /**
     * 初始化数据
     */
    protected open fun initData() {}

    /**
     * 初始化观察者（LiveData / Flow）
     */
    protected open fun initObserver() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // ❗ 必须释放
    }
}