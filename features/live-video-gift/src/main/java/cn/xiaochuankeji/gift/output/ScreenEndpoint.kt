package cn.xiaochuankeji.gift.output

import cn.xiaochuankeji.gift.GLRenderer
import cn.xiaochuankeji.gift.ProcessingPipeline
import cn.xiaochuankeji.gift.input.GLTextureInputRenderer
import cn.xiaochuankeji.gift.input.GLTextureOutputRenderer

class ScreenEndpoint(pipeline: ProcessingPipeline) : GLRenderer(), GLTextureInputRenderer {
    val pipeline = pipeline

    override fun newTextureReady(texture: Int, source: GLTextureOutputRenderer, newData: Boolean) {
        texture_in = texture
        setWidth(source.getWidth())
        setHeight(source.getHeight())
        if(getWidth() != pipeline.width || getHeight() != pipeline.height){
            setRenderSize(pipeline.width, pipeline.height)
        }
        onDrawFrame()
    }

}