package com.violin.views.views.fallingview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.violin.base.act.UIUtil

class FallingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {
    private var mFlakesDensity = DEFAULT_FLAKES_DENSITY
    private var mDelay = DEFAULT_DELAY
    private var mFlakes: Array<Flake> = emptyArray()
    private var mFlakeBitmap: Bitmap? = null
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mWidth = 0
    private var mHeight = 0
    private var mFlakeSize = 0
    private var mAnimTime = 3 * 1000L
    private val mRunnable = Runnable { invalidate() }
    private val mCloseRunnable = Runnable {
        if (this.parent is ViewGroup) {
            ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
                duration = 300
                start()
            }.doOnEnd {
                (parent as ViewGroup).removeView(this)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
        handler?.postDelayed(mCloseRunnable, mAnimTime)
    }

    private fun initView() {
        setBackgroundColor(Color.TRANSPARENT)
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            mWidth = w
            mHeight = h
            initDensity(w, h, mFlakeSize)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        for (flake in mFlakes) {
            flake.draw(canvas, mFlakeBitmap!!)
        }
        handler?.postDelayed(mRunnable, mDelay.toLong())
    }


    override fun onDetachedFromWindow() {
        handler?.removeCallbacks(mRunnable)
        handler?.removeCallbacks(mCloseRunnable)
        super.onDetachedFromWindow()
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
        if (mWidth > 0 && mHeight > 0) {
            initDensity(mWidth, mHeight, mFlakeSize)
        }
    }

    fun setAnimTime(animTimeSecond: Int) {
        this.mAnimTime = animTimeSecond * 1000L
    }

    fun setDelay(delay: Int) {
        this.mDelay = delay
    }

    data class GiftFallingJson(
        var gift_id: Long? = null,
        var icon: String? = null,
        var rain_level: Int? = null,// 礼物等级
        var count: Int? = null
    )

    companion object {
        private const val DEFAULT_FLAKES_DENSITY = 80// 默认礼物数
        private const val DEFAULT_DELAY = 10// 默认刷新时间

        fun startAnim(giftFallingJson: GiftFallingJson, context: Context, container: ViewGroup) {
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
                            val fallingView = FallingView(context)
                                .apply {
                                    setBitmap(resource, size)
                                    setDensity(60)
                                    setDelay(10)
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
