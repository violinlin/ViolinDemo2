package com.violin.views.views.fallingview.snowfalll

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.violin.base.act.UIUtil

class SnowfallView(context: Context, val config: SnowParamsConfig) : View(context) {
    private lateinit var updateSnowflakesThread: UpdateSnowflakesThread
    private var snowflakes: Array<Snowflake>? = null
    var closeAnim: ObjectAnimator? = null

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    private val mCloseRunnable = Runnable {
        closeAnim = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
            duration = 300
            start()
        }
        closeAnim?.doOnEnd {
            close()
        }

    }

    private fun close() {
        if (this.parent is ViewGroup) {
            (parent as ViewGroup).removeView(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateSnowflakesThread = UpdateSnowflakesThread()
        if (config.animTimeSecond > 0) {
            handler?.postDelayed(mCloseRunnable, config.animTimeSecond * 1000L)
        }
    }

    override fun onDetachedFromWindow() {
        release()
        super.onDetachedFromWindow()
    }

    private fun release() {
        closeAnim?.cancel()
        updateSnowflakesThread.quit()
        handler?.removeCallbacks(mCloseRunnable)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        snowflakes = createSnowflakes()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (changedView === this && visibility == GONE) {
            snowflakes?.forEach { it.reset() }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isInEditMode) {
            return
        }

        var haveAtLeastOneVisibleSnowflake = false

        val localSnowflakes = snowflakes
        if (localSnowflakes != null) {
            for (snowflake in localSnowflakes) {
                if (snowflake.isStillFalling()) {
                    haveAtLeastOneVisibleSnowflake = true
                    snowflake.draw(canvas)
                }
            }
        }

        if (haveAtLeastOneVisibleSnowflake) {
            updateSnowflakes()
        } else {
            visibility = GONE
        }

        val fallingSnowflakes = snowflakes?.filter { it.isStillFalling() }
        if (fallingSnowflakes?.isNotEmpty() == true) {
            fallingSnowflakes.forEach { it.draw(canvas) }
            updateSnowflakes()
        } else {
            visibility = GONE
        }
    }


    private fun createSnowflakes(): Array<Snowflake> {
        val randomizer = Randomizer()
        config.parentWidth = width
        config.parentHeight = height
        return Array(config.snowflakesNum) { Snowflake(randomizer, config) }
    }

    private fun updateSnowflakes() {
        updateSnowflakesThread.handler.post {
            var haveAtLeastOneVisibleSnowflake = false

            val localSnowflakes = snowflakes ?: return@post

            for (snowflake in localSnowflakes) {
                if (snowflake.isStillFalling()) {
                    haveAtLeastOneVisibleSnowflake = true
                    snowflake.update()
                }
            }

            if (haveAtLeastOneVisibleSnowflake) {
                postInvalidateOnAnimation()
            }
        }
    }

    private class UpdateSnowflakesThread : HandlerThread("SnowflakesComputations") {
        val handler: Handler

        init {
            start()
            handler = Handler(looper)
        }
    }

    companion object {
        const val DEFAULT_SNOWFLAKE_ALPHA_MIN = 150
        const val DEFAULT_SNOWFLAKE_ALPHA_MAX = 250
        const val DEFAULT_SNOWFLAKE_ANGLE_MAX = 5
        const val DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP = 60
        const val DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP = 80
        const val DEFAULT_SNOWFLAKE_SPEED_MIN = 6
        const val DEFAULT_SNOWFLAKE_SPEED_MAX = 12
        const val DEFAULT_SNOWFLAKES_FADING_ENABLED = true
        const val DEFAULT_SNOWFLAKES_ALREADY_FALLING = false
        private fun setDefaultConfigParams(config: SnowParamsConfig, viewContainer: ViewGroup) {
            if (config.sizeMaxInPx == 0) {
                config.sizeMaxInPx =
                    UIUtil.dp2px(DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP.toFloat(), viewContainer.context)
                        .toInt()
            }
            if (config.sizeMinInPx == 0) {
                config.sizeMinInPx =
                    UIUtil.dp2px(DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP.toFloat(), viewContainer.context)
                        .toInt()
            }
        }

        fun startAnim(config: SnowParamsConfig, viewContainer: ViewGroup) {
            config.imageUrl?.let {
                setDefaultConfigParams(config, viewContainer)
                val bitmapSize = config.sizeMaxInPx
                Glide.with(viewContainer.context)
                    .asBitmap()
                    .override(bitmapSize, bitmapSize)
                    .load(it)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            try {
                                config.image = resource
                                val snowfallView = SnowfallView(viewContainer.context, config)
                                viewContainer.removeAllViews()
                                viewContainer.addView(
                                    snowfallView, ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                    })
            }


        }

    }
}
