package com.violin.views.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.violin.views.databinding.ViewFirstRechargeViewBinding

/**
 * 首充降级弹窗
 */
class FirstRechargeDowngradeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var mBinding: ViewFirstRechargeViewBinding? = null

    init {
        mBinding = ViewFirstRechargeViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    }
}