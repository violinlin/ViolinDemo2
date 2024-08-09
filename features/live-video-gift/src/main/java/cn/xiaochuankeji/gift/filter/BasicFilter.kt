package cn.xiaochuankeji.gift.filter

import cn.xiaochuankeji.gift.input.GLTextureInputRenderer
import cn.xiaochuankeji.gift.input.GLTextureOutputRenderer

open class BasicFilter : GLTextureOutputRenderer(),
    GLTextureInputRenderer {

    override fun newTextureReady(texture: Int, source: GLTextureOutputRenderer, newData: Boolean) {
        if(newData){
            markAsDirty()
        }
        texture_in = texture
        setWidth(source.getWidth())
        setHeight(source.getHeight())
        onDrawFrame()
    }


}