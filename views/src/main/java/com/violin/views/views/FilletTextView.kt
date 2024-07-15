package com.violin.views.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class FilletTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    var rect: RectF? = null
    var radii = floatArrayOf(50F, 50F, 50F, 50F, 50F, 50F, 50F, 50F)
    private var paintFill: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        updateRect(w, h)
        rect = RectF(w.toFloat() / 2f, 0f, width.toFloat(), height.toFloat())
    }

    override fun onDraw(canvas: Canvas) {

        if (rect != null) {
            paintFill.color = Color.RED
            val mPath = Path()
            mPath.addRoundRect(
                rect!!, radii, Path.Direction.CCW
            )
            canvas.drawPath(mPath, paintFill)

        }
        super.onDraw(canvas)
    }
}