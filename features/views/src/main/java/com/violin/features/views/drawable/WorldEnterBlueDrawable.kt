package com.violin.features.views.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Shader
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.graphics.toColorInt

class WorldEnterBlueDrawable : Drawable() {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val slantStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val topStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bottomStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bodyPath = Path()
    private val slantStrokePath = Path()
    private val topStrokePath = Path()
    private val bottomStrokePath = Path()

    private val strokeWidth = 4f * Resources.getSystem().displayMetrics.density

    private val strokeStartColor = "#000080FF".toColorInt()
    private val strokeEndColor = "#FF0080FF".toColorInt()
    override fun draw(canvas: Canvas) {
        val w = bounds.width().toFloat()
        val h = bounds.height().toFloat()
        val slantWidth = h * 0.38f
        val outerTopX = w
        val outerBottomX = w - slantWidth
        val innerTopX = outerTopX - strokeWidth
        val innerBottomX = outerBottomX - strokeWidth
        val overlap = 1f
        val bottomStrokeExtend = 0f * Resources.getSystem().displayMetrics.density

        bodyPath.reset()
        bodyPath.moveTo(0f, 0f)
        bodyPath.lineTo(innerTopX, 0f)
        bodyPath.lineTo(innerBottomX, h)
        bodyPath.lineTo(0f, h)
        bodyPath.close()

        backgroundPaint.color = "#80000000".toColorInt()
        canvas.drawPath(bodyPath, backgroundPaint)

        bodyPaint.shader = LinearGradient(
            0f,
            0f,
            innerTopX,
            0f,
            Color.parseColor("#1A0055FF"),
            Color.parseColor("#FF0055FF"),
            Shader.TileMode.CLAMP
        )
        canvas.drawPath(bodyPath, bodyPaint)

        topStrokePaint.shader = LinearGradient(
            0f,
            0f,
            w,
            0f,
            strokeStartColor,
            strokeEndColor,
            Shader.TileMode.CLAMP
        )
        topStrokePath.reset()
        topStrokePath.moveTo(0f, 0f)
        topStrokePath.lineTo(outerTopX, 0f)
        topStrokePath.lineTo(innerTopX, strokeWidth)
        topStrokePath.lineTo(0f, strokeWidth)
        topStrokePath.close()
        canvas.drawPath(topStrokePath, topStrokePaint)

        bottomStrokePaint.shader = LinearGradient(
            0f,
            0f,
            outerBottomX,
            0f,
            strokeStartColor,
            strokeEndColor,
            Shader.TileMode.CLAMP
        )
        bottomStrokePath.reset()
        bottomStrokePath.moveTo(0f, h - strokeWidth)
        bottomStrokePath.lineTo(outerBottomX + bottomStrokeExtend, h - strokeWidth)
        bottomStrokePath.lineTo(outerBottomX + bottomStrokeExtend, h)
        bottomStrokePath.lineTo(0f, h)
        bottomStrokePath.close()
        canvas.drawPath(bottomStrokePath, bottomStrokePaint)

        slantStrokePath.reset()
        slantStrokePath.moveTo(innerTopX, 0f)
        slantStrokePath.lineTo(outerTopX, 0f)
        slantStrokePath.lineTo(outerBottomX + overlap, h + overlap)
        slantStrokePath.lineTo(innerBottomX - overlap, h + overlap)
        slantStrokePath.close()

        slantStrokePaint.color = strokeEndColor
        slantStrokePaint.style = Paint.Style.FILL
        canvas.drawPath(slantStrokePath, slantStrokePaint)
    }

    override fun setAlpha(alpha: Int) {
        backgroundPaint.alpha = alpha
        bodyPaint.alpha = alpha
        slantStrokePaint.alpha = alpha
        topStrokePaint.alpha = alpha
        bottomStrokePaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        backgroundPaint.colorFilter = colorFilter
        bodyPaint.colorFilter = colorFilter
        slantStrokePaint.colorFilter = colorFilter
        topStrokePaint.colorFilter = colorFilter
        bottomStrokePaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}
