package com.violin.views.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.widget.FrameLayout
import com.violin.base.act.UIUtil
import kotlin.math.asin

class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var sweepGradient: SweepGradient? = null
    private var sweepAngle = 0
    private var mPaint: Paint
    private val valueStartAnimator by lazy {
        ValueAnimator.ofInt(360, 0)
    }
    private var colors = intArrayOf(
        Color.parseColor("#6B37F1"), Color.parseColor("#FF1FA5"),
        Color.parseColor("#FFEC3D")
    )
    private val strokeWidth by lazy {
        UIUtil.dp2px(7f, context)
    }
    private var oval: RectF? = null

    init {
        rotationY = 180F
        setWillNotDraw(false)
        mPaint = Paint()
        // 设置画笔为抗锯齿
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        mPaint.strokeCap = Paint.Cap.ROUND
        setOnClickListener {
            startAnimator()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (sweepGradient == null) {
            sweepGradient =
                SweepGradient((width / 2).toFloat(), (height / 2).toFloat(), colors, null)
            mPaint.shader = sweepGradient
            val matrix = Matrix()
            val angleOffset =
                Math.toDegrees(asin(strokeWidth / 2f / (width/2 - strokeWidth / 2f)).toDouble()).toFloat()

            matrix.setRotate(270f - angleOffset, (width / 2).toFloat(), (height / 2).toFloat())
            sweepGradient?.setLocalMatrix(matrix)
        }
//        canvas.drawArc(oval!!, 270f, 360f, false, mPaintBg)
        canvas.drawArc(oval!!, 270f, sweepAngle.toFloat(), false, mPaint)
    }

    private fun startAnimator() {
        valueStartAnimator.cancel()
        valueStartAnimator.removeAllUpdateListeners()
        valueStartAnimator.removeAllListeners()
        valueStartAnimator.addUpdateListener { animation: ValueAnimator ->
            sweepAngle = animation.animatedValue as Int
            invalidate()
        }
        valueStartAnimator.duration = 7 * 1000
        valueStartAnimator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        oval = RectF(
            (strokeWidth / 2 + 1),
            (strokeWidth / 2 + 1),
            (width - strokeWidth / 2 - 1),
            (height - strokeWidth / 2 - 1)
        )
    }

}