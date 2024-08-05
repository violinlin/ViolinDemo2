package com.violin.base.act

import android.content.Context
import android.util.TypedValue

object UIUtil {
    fun dp2px(dpValue: Float, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        )
    }
}