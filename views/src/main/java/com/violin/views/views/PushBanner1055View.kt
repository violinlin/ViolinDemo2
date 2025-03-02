package com.violin.views.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.violin.views.databinding.ViewPushBanner1055ViewBinding

class PushBanner1055View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    var mBinding: ViewPushBanner1055ViewBinding

    init {
        mBinding = ViewPushBanner1055ViewBinding.inflate(LayoutInflater.from(context), this, true)

    }

    private fun initView() {
        val data = PushBannerStyle1055Bean(

        )
    }

    fun setData(data: PushBannerStyle1055Bean) {
        when (data.bg_type) {
            0 -> {
                // 动图

            }

            1 -> {
                //png
            }

            2 -> {
                // 代码渐变
            }
        }


    }
}