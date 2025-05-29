package com.violin.views.views.fallingview

import java.util.Random
import kotlin.math.*


object Random {
    private val RANDOM = Random()
    fun getRandom(lower: Float, upper: Float): Float {
        val min = min(lower.toDouble(), upper.toDouble()).toFloat()
        val max = max(lower.toDouble(), upper.toDouble()).toFloat()
        return getRandom(max - min) + min
    }

    fun getRandom(upper: Float): Float {
        return RANDOM.nextFloat() * upper
    }

    fun getRandom(upper: Int): Int {
        return RANDOM.nextInt(upper)
    }

    fun roundAwayFromZero(value: Double): Double {
        return if (value > 0) {
            ceil(value)
        } else {
            floor(value)
        }
    }

}
