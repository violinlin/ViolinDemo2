package com.violin.views.views.fallingview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import kotlin.math.cos
import kotlin.math.sin

class Flake(
    private val mPoint: Point,
    private var mAngle: Float,
    private val mIncrement: Float,
    private val mPaint: Paint,
    private val config: FallingViewConfig,
    private val fakeSize: Int
) {

    fun draw(canvas: Canvas, flakeBitmap: Bitmap) {
        val width = canvas.width
        val height = canvas.height
        move(width, height)
        val dstRect =
            Rect(mPoint.x, mPoint.y, mPoint.x + fakeSize, mPoint.y + fakeSize)
        canvas.drawBitmap(flakeBitmap, null, dstRect, null)
    }

    private fun move(width: Int, height: Int) {
        val xIncrement = (mIncrement * cos(mAngle.toDouble()))
        var x = mPoint.x.toDouble()
        if (config.isMoveX) {
            x = mPoint.x + mRandom.roundAwayFromZero(xIncrement)
        }
        var yIncrement = (mIncrement * sin(mAngle.toDouble())) + config.ySpeedBuffer
        Log.d("Fake", "yIncrement:${yIncrement}")
        if (config.direction == 1) {
            yIncrement = -yIncrement
        }
        val y = mPoint.y + yIncrement
        mAngle += mRandom.getRandom(-ANGLE_SEED, ANGLE_SEED) / ANGLE_DIVISOR
        mPoint.set(x.toInt(), y.toInt())
        if (!isInside(width, height)) {
            reset(width, height)
        }
    }

    private fun isInside(width: Int, height: Int): Boolean {
        val flakeSize = fakeSize
        val x = mPoint.x
        val y = mPoint.y
        return x >= -flakeSize - 1 && x - flakeSize <= width && y >= -flakeSize - 1 && y - flakeSize < height
    }

    private fun reset(width: Int, height: Int) {
        val flakeSize = fakeSize
        mPoint.x = mRandom.getRandom(0F, width - flakeSize.toFloat()).toInt()
        if (config.direction == 1) {
            mPoint.y = height - flakeSize
        } else {
            mPoint.y = (-flakeSize - 1).toInt()
        }
        mAngle =
            mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE
    }


    companion object {
        private val TAG: String = Flake::class.java.name
        private const val ANGE_RANGE = 0.1f
        private const val HALF_ANGLE_RANGE = ANGE_RANGE / 2f
        private const val HALF_PI = Math.PI.toFloat() / 2f
        private const val ANGLE_SEED = 25f
        private const val ANGLE_DIVISOR = 10000f
        private const val INCREMENT_LOWER = 2f
        private const val INCREMENT_UPPER = 4f
        private val mRandom = Random

        @JvmStatic
        fun create(width: Int, height: Int, paint: Paint, config: FallingViewConfig): Flake {
            var flakeSize = config.iconSizePX
            config.sizeScale?.let { sizeScale ->
                flakeSize = mRandom.getRandom(
                    config.iconSizePX.toFloat(),
                    config.iconSizePX * (1 + sizeScale)
                ).toInt()
            }
            val x = mRandom.getRandom(0F, width - config.iconSizePX.toFloat()).toInt()
            val y = mRandom.getRandom(height)
            val positon = Point(x, y)
            val angle =
                mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE
            val increment = mRandom.getRandom(INCREMENT_LOWER, INCREMENT_UPPER)
            return Flake(positon, angle, increment, paint, config = config, fakeSize = flakeSize)
        }
    }
}
