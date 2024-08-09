package cn.xiaochuankeji.gift.input

import cn.xiaochuankeji.gift.input.GLTextureOutputRenderer


interface GLTextureInputRenderer {
    /**
     * Signals that a new texture is available and the image should be reprocessed.
     * @param texture
     * The texture id to be used as input.
     * @param source
     * The GLTextureOutputRenderer which produced the texture.
     */
    fun newTextureReady(
        texture: Int,
        source: GLTextureOutputRenderer,
        newData: Boolean
    )
}