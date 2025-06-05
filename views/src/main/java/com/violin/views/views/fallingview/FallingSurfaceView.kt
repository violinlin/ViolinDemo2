package com.violin.views.views.fallingview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.violin.base.act.UIUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class FallingSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private var mFlakesDensity = DEFAULT_FLAKES_DENSITY
    private var mDelay = DEFAULT_DELAY
    private var mFlakes: ArrayList<Flake> = ArrayList()
    private var mFlakeBitmap: Bitmap? = null
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mWidth = 0
    private var mHeight = 0
    private var mConfig: FallingViewConfig? = null
    private var mAnimTime = 3 * 1000L
    private var animationFallingJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var mAddCount: AtomicInteger? = null
    val TAG = "FallingSurfaceView"

    init {
        initView()
    }

    private val mCloseRunnable = Runnable {
        ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
            duration = 300
            start()
        }.doOnEnd {
            close()
        }

    }

    private fun close() {
        if (this.parent is ViewGroup) {
            (parent as ViewGroup).removeView(this)
        }
    }

    private fun initView() {
        setZOrderOnTop(true) // 置于顶层
        holder.setFormat(PixelFormat.TRANSLUCENT) // 设置像素格式为支持透明
        holder.addCallback(this)
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
    }


    private fun initDensity(w: Int, h: Int, configData: FallingViewConfig) {
        for (i in 0..<mFlakesDensity) {
            val fake = Flake.create(w, h, mPaint, config = configData)
            mFlakes.add(fake)
        }
    }

    fun setBitmap(bitmap: Bitmap, config: FallingViewConfig) {
        mFlakeBitmap = bitmap
        mConfig = config
    }


    fun setDensity(density: Int) {
        this.mFlakesDensity = density
    }

    fun setAnimTime(animTimeSecond: Int) {
        this.mAnimTime = animTimeSecond * 1000L
    }

    fun setDelay(delay: Long) {
        this.mDelay = delay
    }

    fun addFakes(addCount: Int) {
        mAddCount = AtomicInteger(addCount)
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
//        handler?.postDelayed(mCloseRunnable, mAnimTime)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (mWidth != width || mHeight != height) {
            mWidth = width
            mHeight = height
            mConfig?.let {
                initDensity(mWidth, mHeight, it)
            }
            startFalling()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopFalling()
    }

    override fun onDetachedFromWindow() {
        stopFalling()
        handler?.removeCallbacks(mCloseRunnable)
        super.onDetachedFromWindow()
    }

    private fun startFalling() {
        stopFalling()
        animationFallingJob = coroutineScope.launch {
            while (isActive) {
                drawToSurface()
                delay(mDelay)
                Log.d(TAG, "thread drawToSurface:${Thread.currentThread().name}")
            }
        }
    }

    private fun drawToSurface() {
        var canvas: Canvas? = null
        try {
            if (useHardware) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    canvas = holder.lockHardwareCanvas()
                }
            } else {
                canvas = holder.lockCanvas()
            }
            canvas?.let {
                canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)
                val addCount = mAddCount?.get() ?: 0
                if (addCount > 0) {
                    mConfig?.let {
                        for (i in 0..<addCount) {
                            mFlakes.add(Flake.create(mWidth, mHeight, mPaint, it))
                        }
                    }
                    mFlakesDensity += addCount
                    mAddCount = null
                }
                for (flake in mFlakes) {
                    flake.draw(canvas, mFlakeBitmap!!)
                }
            }
        } catch (e: Exception) {
            post {
                close()
            }
            e.printStackTrace()
        } finally {
            canvas?.let {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    private fun stopFalling() {
        animationFallingJob?.cancel()
        animationFallingJob = null
    }

    companion object {
        private const val DEFAULT_FLAKES_DENSITY = 80// 默认礼物数
        private const val DEFAULT_DELAY = 10L// 默认刷新时间
        var useHardware = false


        fun startAnim(
            configData: FallingViewConfig,
            context: Context,
            container: ViewGroup,
            startCallback: ((FallingSurfaceView) -> Unit)? = null
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                useHardware = true
            }
            val size = configData.iconSizePX
            configData.icon?.let {
                Glide.with(context)
                    .asBitmap()
                    .override(size, size)
                    .load(it)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            val fallingView = FallingSurfaceView(context)
                                .apply {
                                    setBitmap(resource, configData)
                                    configData.delayTime?.let { delay ->
                                        setDelay(delay.toLong())
                                    }
                                    if (useHardware) {
                                        setDensity(configData.maxDensity)
                                    } else {
                                        setDensity(Math.min(configData.maxDensity, 15))
                                        setDelay(5)
                                    }
                                    configData.animTimeSecond?.let {
                                        setAnimTime(it)
                                    }
                                }
                            container.removeAllViews()
                            container.addView(
                                fallingView, ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            )
                            startCallback?.invoke(fallingView)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }
    }
}
