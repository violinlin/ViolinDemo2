package com.violin.views.views.fallingview.snowfalll

import java.util.Random
import kotlin.math.abs

class Randomizer {

    // No need in random instance to be lazy
    private val random = Random(System.currentTimeMillis())

    fun randomDouble(max: Int): Double {
        return random.nextDouble() * (max + 1)
    }

    fun randomInt(min: Int, max: Int, gaussian: Boolean = false): Int {
        return randomInt(max - min, gaussian) + min
    }

    fun randomInt(max: Int, gaussian: Boolean = false): Int {
        return if (gaussian) {
            (abs(randomGaussian()) * (max + 1)).toInt()
        } else {
            random.nextInt(max + 1)
        }
    }

    fun randomGaussian(): Double {
        val gaussian = random.nextGaussian() / 3 // more 99% of instances in range (-1, 1)
        return if (gaussian > -1 && gaussian < 1) gaussian else randomGaussian()
    }

    fun randomSignum(): Int {
        return if (random.nextBoolean()) 1 else -1
    }
}