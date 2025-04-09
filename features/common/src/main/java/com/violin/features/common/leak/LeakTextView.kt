package com.violin.features.common.leak

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import java.util.logging.Handler


class LeakTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        postDelayed({
            Log.d("Leak", "delay message....")
        }, 10 * 1000)

    }

    override fun onDetachedFromWindow() {
        Log.d("Leak", "leaktextview onDetachedFromWindow")
        super.onDetachedFromWindow()
    }
}