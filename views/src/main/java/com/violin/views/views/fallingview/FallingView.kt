package com.violin.views.views.fallingview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.violin.views.R
import com.violin.views.views.fallingview.FallingView
import com.violin.views.views.fallingview.Flake.Companion.create
import androidx.core.graphics.scale

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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
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
        handler.postDelayed(mRunnable, mDelay.toLong())
    }

    private val mRunnable = Runnable { invalidate() }


    private fun initDensity(w: Int, h: Int, flakeSize: Int) {
        mFlakes = Array(mFlakesDensity) {
            create(w, h, mPaint, flakeSize)
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

    fun setDelay(delay: Int) {
        this.mDelay = delay
    }

    companion object {
        private const val DEFAULT_FLAKES_DENSITY = 80// 默认礼物数
        private const val DEFAULT_DELAY = 10// 默认刷新时间
    }
}
