package cn.xiaochuankeji.gift.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import cn.xiaochuankeji.gift.ProcessingPipeline

/**
 * 主要是控制大小
 */
class EffectGLTextureView : GLTextureView {
    var customMode = false

    var videoWidth = 0
    var videoHeight = 0
    var scaleType = CENTER_CROP

    companion object {
        const val FIT_CENTER = 0b1.shl(0)
        const val CENTER_CROP = 0b1.shl(1)
        const val HEIGHT_CROP = 0b1.shl(2)  //横向沾满，高度移除
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        debugFlags = DEBUG_CHECK_GL_ERROR or DEBUG_LOG_GL_CALLS
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        isOpaque = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        videoHeight = 0
        videoWidth = 0
        customMode = false
    }

    fun setPipeline(pipeline: ProcessingPipeline) {
        setRenderer(pipeline)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun setRenderSize(width: Int, height: Int) {
        if (width == 0 && height == 0) {
            customMode = false
            return
        }
        customMode = true
        if (this.videoWidth != width || this.videoHeight != height) {
            this.videoWidth = width
            this.videoHeight = height
            post(Runnable {
                requestLayout()
            })
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!customMode) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        val width = videoWidth
        val height = videoHeight
        val viewWidth = View.getDefaultSize(width, widthMeasureSpec)
        val viewHeight = View.getDefaultSize(height, heightMeasureSpec)

        val scaleX = viewWidth * 1.0f / width
        val scaleY = viewHeight * 1.0f / height
        var scale = 1f
        if (scaleType == CENTER_CROP) {
            scale = Math.max(scaleX, scaleY)

        } else if (scaleType == FIT_CENTER) {
            scale = Math.min(scaleX, scaleY)
        } else if (scaleType == HEIGHT_CROP) {
            scale = scaleX
        }
        val resultX = (viewWidth * (scale / scaleX)).toInt()
        val resultY = (viewHeight * (scale / scaleY)).toInt()
        setMeasuredDimension(resultX, resultY)
    }
}