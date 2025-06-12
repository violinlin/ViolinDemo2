package com.violin.views.views.fallingview.snowfalll

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger


class SnowfallSurfaceView(context: Context, val config: SnowParamsConfig) : SurfaceView(context),
    SurfaceHolder.Callback, SnowFallInterface {
    private var snowflakes: ArrayList<Snowflake> = ArrayList()
    var closeAnim: ObjectAnimator? = null
    val randomizer = Randomizer()

    private var animationFallingJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var mWidth = 0
    private var mHeight = 0
    private var mAddCount: AtomicInteger? = null
    private var isClose = false

    init {
        setZOrderOnTop(true) // 置于顶层
        holder.setFormat(PixelFormat.TRANSLUCENT) // 设置像素格式为支持透明
        holder.addCallback(this)
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
        if (isClose) {
            return
        }
        if (this.parent is ViewGroup) {
            (parent as ViewGroup).removeView(this)
            isClose = true
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (config.animTimeSecond > 0) {
            handler?.postDelayed(mCloseRunnable, config.animTimeSecond * 1000L)
        }
    }

    override fun onDetachedFromWindow() {
        release()
        super.onDetachedFromWindow()
    }

    private fun release(removeCloseRunnable: Boolean = true) {
        stopFalling()
        closeAnim?.cancel()
        animationFallingJob?.cancel()
        if (removeCloseRunnable) {
            handler?.removeCallbacks(mCloseRunnable)
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun addFakes(amount: Int) {
        mAddCount = AtomicInteger(amount)
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (mWidth != width || mHeight != height) {
            mWidth = width
            mHeight = height
            snowflakes = createSnowflakes()
            startFalling()
        }


    }

    private fun startFalling() {
        stopFalling()
        animationFallingJob = coroutineScope.launch {
            while (isActive) {
                drawToSurface()
            }
        }

    }

    private fun stopFalling() {
        animationFallingJob?.cancel()
        animationFallingJob = null
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        release(removeCloseRunnable = false)
    }


    private fun drawToSurface() {
        var canvas: Canvas? = null
        try {
            if (!holder.surface.isValid) {
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas = holder.lockHardwareCanvas()
            }
            canvas?.let {
                canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)
                val addCount = mAddCount?.get() ?: 0
                if (addCount > 0) {
                    for (i in 0 until addCount) {
                        snowflakes.add(Snowflake(randomizer, config))
                    }
                    mAddCount = null
                }
                val localSnowflakes = snowflakes
                if (localSnowflakes != null) {
                    for (snowflake in localSnowflakes) {
                        snowflake.draw(canvas)
                    }
                    updateSnowflakes()
                }
            }
            canvas?.let {
                holder.unlockCanvasAndPost(canvas)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            post {
                close()
            }
        }
    }


    private fun createSnowflakes(): ArrayList<Snowflake> {
        config.parentWidth = width
        config.parentHeight = height
        snowflakes.clear()
        for (i in 0 until config.snowflakesNum) {
            snowflakes.add(Snowflake(randomizer, config))
        }
        return snowflakes
    }

    private fun updateSnowflakes() {
        val localSnowflakes = snowflakes ?: return
        for (snowflake in localSnowflakes) {
            snowflake.update()
        }
    }

}
