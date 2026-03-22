package com.violin.features.common.proto

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.violin.fretures.common.R


class ProtoTextView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet) :
    AppCompatTextView(context, attributeSet) {
        init {
            val array  = context.obtainStyledAttributes(attributeSet, R.styleable.ProtoTextView)
            val key = array.getString(R.styleable.ProtoTextView_protoKey)
            key?.let {
                setText(it)
            }

        }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
    }
}