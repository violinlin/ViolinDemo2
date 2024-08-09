package cn.xiaochuankeji.gift.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import cn.xiaochuankeji.gift.ProcessingPipeline
import cn.xiaochuankeji.gift.player.EffectPlayer

class PlayerContainerView : FrameLayout {

    protected var mGravity: Int = Gravity.CENTER
    protected var mScaleType: Int = EffectPlayer.CENTER_CROP
    var mGlTextureView: EffectGLTextureView? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    //重置GLTextureView
    fun reset() {
        detach()
        mGlTextureView = EffectGLTextureView(context)
    }

    fun getGLTextureView(): EffectGLTextureView {
        return mGlTextureView!!
    }

    fun setPipeline(pipeline: ProcessingPipeline) {
        mGlTextureView?.setPipeline(pipeline)
    }

    fun setGravity(gravity: Int) {
        mGravity = gravity
        if (mGlTextureView?.parent != null) {
            (mGlTextureView?.layoutParams as FrameLayout.LayoutParams).gravity = mGravity
        }
    }

    fun setScaleType(type: Int) {
        mScaleType = type
        mGlTextureView?.scaleType = type
    }

    fun attach() {
        detach()
        val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = mGravity
        mGlTextureView?.scaleType = mScaleType
        addView(
                mGlTextureView,
                params
        )
        visibility = View.VISIBLE
    }

    fun detach() {
        if (childCount > 0) {
            removeAllViews()
        }
        visibility = View.GONE
    }
}