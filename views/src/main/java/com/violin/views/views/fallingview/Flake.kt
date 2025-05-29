package com.violin.views.views.fallingview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import kotlin.math.cos
import kotlin.math.sin

class Flake(
    private val mPoint: Point,
    private var mAngle: Float,
    private val mIncrement: Float,
    private val mFlakeSize: Int,
    private val mPaint: Paint
) {

    fun draw(canvas: Canvas, flakeBitmap: Bitmap) {
        val width = canvas.width
        val height = canvas.height
        move(width, height)
        canvas.drawBitmap(flakeBitmap, mPoint.x.toFloat(), mPoint.y.toFloat(), mPaint)
    }

    private fun move(width: Int, height: Int) {
        val xIncrement = (mIncrement * cos(mAngle.toDouble()))
        val x = mPoint.x + mRandom.roundAwayFromZero(xIncrement)
        val yIncrement = (mIncrement * sin(mAngle.toDouble()))
        val y = mPoint.y + yIncrement
        mAngle += mRandom.getRandom(-ANGLE_SEED, ANGLE_SEED) / ANGLE_DIVISOR
        mPoint.set(x.toInt(), y.toInt())
        if (!isInside(width, height)) {
            reset(width)
        }
    }

    private fun isInside(width: Int, height: Int): Boolean {
        val x = mPoint.x
        val y = mPoint.y
        return x >= -mFlakeSize - 1 && x - mFlakeSize <= width && y >= -mFlakeSize - 1 && y - mFlakeSize < height
    }

    private fun reset(width: Int) {
        mPoint.x = mRandom.getRandom(width)
        mPoint.y = (-mFlakeSize - 1).toInt()
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
        fun create(width: Int, height: Int, paint: Paint, flakeSize: Int): Flake {
            val x = mRandom.getRandom(width)
            val y = mRandom.getRandom(height)
            val positon = Point(x, y)
            val angle =
                mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE
            val increment = mRandom.getRandom(INCREMENT_LOWER, INCREMENT_UPPER)
            return Flake(positon, angle, increment, flakeSize, paint)
        }
    }
}
