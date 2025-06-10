package com.violin.views.views.fallingview.snowfalll

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Rect
import android.util.Log
import java.lang.Math.toRadians

internal class Snowflake(
    private val randomizer: Randomizer,
    private val params: SnowParamsConfig,
) {
    private var size: Int = 0
    private var alpha: Int = 255
    private var minAlpha :Int = 100
    private var bitmap: Bitmap? = null
    private var speedX: Double = 0.0
    private var speedY: Double = 0.0
    private var positionX: Double = 0.0
    private var positionY: Double = 0.0

    private var _paint: Paint? = null
    private val paint: Paint
        get() {
            if (_paint == null) {
                _paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.WHITE
                    style = Style.FILL
                }
            }

            return _paint!!
        }

    var shouldRecycleFalling = true
    private var stopped = false

    init {
        reset()
    }

    internal fun reset(positionY: Double? = null) {
        Log.d("Snowflake","reset reset alpha ${ paint.alpha} = y ${positionY} size${size}")
        shouldRecycleFalling = true
        size = randomizer.randomInt(params.sizeMinInPx, params.sizeMaxInPx, gaussian = true)
        bitmap = params.image
        val speed =
            ((size - params.sizeMinInPx).toFloat() / (params.sizeMaxInPx - params.sizeMinInPx) *
                    (params.speedMax - params.speedMin) + params.speedMin)
        val angle = toRadians(randomizer.randomDouble(params.angleMax) * randomizer.randomSignum())
        if (params.isMoveX) {
            speedX = speed * kotlin.math.sin(angle)
        }
        speedY = speed * kotlin.math.cos(angle)
        positionX = randomizer.randomDouble(params.parentWidth - size)
        if (positionY != null) {
            this.positionY = positionY
        } else {
            this.positionY = randomizer.randomDouble(params.parentHeight)
            if (!params.alreadyFalling) {
                if (params.direction == 1) {
                    this.positionY = this.positionY + params.parentHeight - size
                } else {
                    this.positionY = this.positionY - params.parentHeight - size
                }
            }
        }
    }

    fun isStillFalling(): Boolean {
        return (shouldRecycleFalling || (positionY > 0 && positionY < params.parentHeight))
    }

    fun update() {
        positionX += speedX
        if (params.direction == 1) {
            positionY -= speedY
            if (positionY < -size) {
                if (shouldRecycleFalling) {
                    if (stopped) {
                        stopped = false
                        reset()
                    } else {
                        reset(positionY = (params.parentHeight + size).toDouble())
                    }
                } else {
                    positionY = -size.toDouble()
                    stopped = true
                }
            }
            if (params.fadingEnabled) {
                val pY = if (positionY > params.parentHeight) {
                    params.parentHeight
                } else if (positionY < 0) {
                    0F
                } else {
                    positionY
                }
                var disAlpha = alpha - minAlpha
                disAlpha =
                    (disAlpha * (1 - ((params.parentHeight - pY.toFloat()) / params.parentHeight))).toInt()
                paint.alpha = minAlpha + disAlpha
            }
        } else {
            positionY += speedY
            if (positionY > params.parentHeight) {
                if (shouldRecycleFalling) {
                    if (stopped) {
                        stopped = false
                        reset()
                    } else {
                        reset(positionY = -size.toDouble())
                    }
                } else {
                    positionY = params.parentHeight + size.toDouble()
                    stopped = true
                }
            }
            if (params.fadingEnabled) {
                paint.alpha =
                    (alpha * ((params.parentHeight - positionY).toFloat() / params.parentHeight)).toInt()
            }
        }
    }

    fun draw(canvas: Canvas) {
        bitmap?.let {
            val x = positionX.toInt()
            val y = positionY.toInt()
            val dstRect =
                Rect(x, y, x + size, y + size)
            canvas.drawBitmap(it, null, dstRect, paint)
        }
    }

}