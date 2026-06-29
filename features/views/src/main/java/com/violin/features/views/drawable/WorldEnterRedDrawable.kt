package com.violin.features.views.drawable

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import androidx.core.graphics.toColorInt

class WorldEnterRedDrawable : Drawable() {

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
    private val bodyStartColor = "#FFFF002B".toColorInt()
    private val bodyEndColor = "#1AFF002B".toColorInt()
    private val strokeStartColor = "#00FF002B".toColorInt()
    private val strokeEndColor = "#FFFF002B".toColorInt()

    override fun draw(canvas: Canvas) {
        val w = bounds.width().toFloat()
        val h = bounds.height().toFloat()
        val slantWidth = h * 0.38f
        val outerTopX = slantWidth
        val outerBottomX = 0f
        val innerTopX = outerTopX + strokeWidth
        val innerBottomX = outerBottomX + strokeWidth
        val overlap = 1f
        val bottomStrokeExtend = 0f * Resources.getSystem().displayMetrics.density

        bodyPath.reset()
        bodyPath.moveTo(innerTopX, 0f)
        bodyPath.lineTo(w, 0f)
        bodyPath.lineTo(w, h)
        bodyPath.lineTo(innerBottomX, h)
        bodyPath.close()

        backgroundPaint.color = "#80000000".toColorInt()
        canvas.drawPath(bodyPath, backgroundPaint)

        bodyPaint.shader = LinearGradient(
            innerTopX,
            0f,
            w,
            0f,
            bodyStartColor,
            bodyEndColor,
            Shader.TileMode.CLAMP
        )
        canvas.drawPath(bodyPath, bodyPaint)

        topStrokePaint.shader = LinearGradient(
            outerTopX,
            0f,
            w,
            0f,
            strokeStartColor,
            strokeEndColor,
            Shader.TileMode.CLAMP
        )
        topStrokePath.reset()
        topStrokePath.moveTo(outerTopX, 0f)
        topStrokePath.lineTo(w, 0f)
        topStrokePath.lineTo(w, strokeWidth)
        topStrokePath.lineTo(innerTopX, strokeWidth)
        topStrokePath.close()
        canvas.drawPath(topStrokePath, topStrokePaint)

        bottomStrokePaint.shader = LinearGradient(
            0f,
            0f,
            w,
            0f,
            strokeStartColor,
            strokeEndColor,
            Shader.TileMode.CLAMP
        )
        bottomStrokePath.reset()
        bottomStrokePath.moveTo(innerBottomX - bottomStrokeExtend, h - strokeWidth)
        bottomStrokePath.lineTo(w, h - strokeWidth)
        bottomStrokePath.lineTo(w, h)
        bottomStrokePath.lineTo(outerBottomX - bottomStrokeExtend, h)
        bottomStrokePath.close()
        canvas.drawPath(bottomStrokePath, bottomStrokePaint)

        slantStrokePath.reset()
        slantStrokePath.moveTo(outerTopX, 0f)
        slantStrokePath.lineTo(innerTopX, 0f)
        slantStrokePath.lineTo(innerBottomX + overlap, h + overlap)
        slantStrokePath.lineTo(outerBottomX - overlap, h + overlap)
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
