package com.violin.views.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class GradientRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 30f
        strokeCap = Paint.Cap.ROUND
    }

    private var sweepGradient: SweepGradient? = null

    private val ringRect = RectF()
    private var startAngle = 0f
    private val sweepAngle = 360f - 45f // 缺口45度

    private val updateRunnable = object : Runnable {
        override fun run() {
            startAngle = (startAngle + 45f) % 360f
            invalidate()
            postDelayed(this, 1000)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post(updateRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(updateRunnable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (min(width, height) - ringPaint.strokeWidth) / 2f

        ringRect.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        // 创建 SweepGradient（只需一次）
        if (sweepGradient == null) {
            sweepGradient = SweepGradient(
                centerX,
                centerY,
                intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED),
                null
            )
        }

        ringPaint.shader = sweepGradient
        canvas.save()
        canvas.rotate(-90f, centerX, centerY) // 让0度在顶部
        canvas.drawArc(ringRect, startAngle, sweepAngle, false, ringPaint)
        canvas.restore()
    }
}
