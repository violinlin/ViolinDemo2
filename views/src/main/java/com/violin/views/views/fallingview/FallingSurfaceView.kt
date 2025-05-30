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

class FallingSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private var mFlakesDensity = DEFAULT_FLAKES_DENSITY
    private var mDelay = DEFAULT_DELAY
    private var mFlakes: Array<Flake> = emptyArray()
    private var mFlakeBitmap: Bitmap? = null
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mWidth = 0
    private var mHeight = 0
    private var mFlakeSize = 0
    private var mAnimTime = 3 * 1000L
    private var animationFallingJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

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


    private fun initDensity(w: Int, h: Int, flakeSize: Int) {
        mFlakes = Array(mFlakesDensity) {
            Flake.create(w, h, mPaint, flakeSize)
        }
    }

    fun setBitmap(bitmap: Bitmap, size: Int) {
        mFlakeSize = size
        mFlakeBitmap = bitmap
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



    override fun surfaceCreated(holder: SurfaceHolder) {
        handler?.postDelayed(mCloseRunnable, mAnimTime)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (mWidth != width || mHeight != height) {
            mWidth = width
            mHeight = height
            initDensity(mWidth, mHeight, mFlakeSize)
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


        fun startAnim(giftFallingJson: GiftFallingJson, context: Context, container: ViewGroup) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                useHardware = true
            }
            giftFallingJson.icon?.let {
                val size = UIUtil.dp2px(60F, context).toInt()
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
                                    setBitmap(resource, size)
                                    if (useHardware) {
                                        setDensity(60)
                                        setDelay(16)
                                    } else {
                                        setDensity(30)
                                        setDelay(10)
                                    }

                                    giftFallingJson.rain_level?.let {
                                        if (it == 1) {
                                            setAnimTime(3)
                                        } else if (it == 2) {
                                            setAnimTime(6)
                                        }
                                    }
                                }
                            container.removeAllViews()
                            container.addView(
                                fallingView, ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            )
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }
    }
}
