package cn.xiaochuankeji.gift

import cn.xiaochuankeji.gift.view.GLTextureView

interface IEffectPlayer {
    fun setRenderSize(width: Int, height: Int)
    fun setGLTextureView(glTextureView: GLTextureView)

    // 目前只支持本地
    fun setDataSource(path: String)
    fun prepare()
    fun startPlay()
    fun stopPlay()
    fun isPlayering(): Boolean

    interface OnCompletionListener {
        fun onCompletion()
    }
}

