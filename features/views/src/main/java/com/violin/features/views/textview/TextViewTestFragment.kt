package com.violin.features.views.textview

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import com.drake.spannable.addSpan
import com.drake.spannable.span.ColorSpan
import com.violin.base.act.BaseFragment
import com.violin.features.views.drawable.WorldEnterBlueDrawable
import com.violin.features.views.drawable.WorldEnterRedDrawable
import com.violin.fretures.common.databinding.FragmentTextViewTestBinding

class TextViewTestFragment : BaseFragment<FragmentTextViewTestBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTextViewTestBinding {
        return FragmentTextViewTestBinding.inflate(inflater, container, false)
    }

    override fun initView() {
    }

    override fun initData() {
        binding.flBlue.background = WorldEnterBlueDrawable()
        binding.flRed.background = WorldEnterRedDrawable()
        binding.tvMarquee.text =
            "这是一个滚动播放的 TextView，用于测试 marquee 效果，内容超过宽度后会从右向左循环滚动。"
        binding.tvMarquee.isSelected = true

        val firstValue = "0"
        val secondValue = "1"
        val thirdValue = "2"
        val fourthValue = "3"
        binding.tvSpan.text = SpannableStringBuilder()
            .addSpan(
                firstValue, arrayOf(
                    ColorSpan(Color.parseColor("#FFFFFFFF")),
                    AbsoluteSizeSpan(36, true)
                )
            )
            .addSpan(
                secondValue, arrayOf(
                    ColorSpan(Color.parseColor("#FFBDB8C8")),
                    AbsoluteSizeSpan(18, true)
                )
            )
            .addSpan(
                "-", arrayOf(
                    ColorSpan(Color.parseColor("#FFBDB8C8")),
                    AbsoluteSizeSpan(18, true)
                )
            )
            .addSpan(
                thirdValue, arrayOf(
                    ColorSpan(Color.parseColor("#FFBDB8C8")),
                    AbsoluteSizeSpan(18, true)
                )
            )
            .addSpan(
                fourthValue, arrayOf(
                    ColorSpan(Color.parseColor("#FFFFFFFF")),
                    AbsoluteSizeSpan(36, true)
                )
            )
    }

}
