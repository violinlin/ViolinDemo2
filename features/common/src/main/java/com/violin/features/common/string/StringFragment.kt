package com.violin.features.common.string

import android.view.LayoutInflater
import android.view.ViewGroup
import com.violin.base.act.BaseFragment
import com.violin.base.act.LogUtil
import com.violin.fretures.common.R
import com.violin.fretures.common.databinding.FragmentStringBinding

class StringFragment : BaseFragment<FragmentStringBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStringBinding {
        // 必须用 Activity 的 Context 创建 inflater，这样 @string/xxx 才会走 Activity 重写的 getResources().getText()
        val activityInflater = LayoutInflater.from(requireActivity())
        return FragmentStringBinding.inflate(activityInflater, container, false)
    }

    override fun initView() {
        // 若 XML 里 @string/string_cdata 仍不走重写，则用代码再设一次（保证走 Activity 的 getResources）
//        binding.text.text = getString(com.violin.fretures.common.R.string.string_cdata)
        binding.text.setOnClickListener {
            LogUtil.d("StringFragment", getString(R.string.md5_e933c4103e12e046d5e1abd64205c6f6))
        }
    }


}