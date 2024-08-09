package cn.xiaochuankeji.gift

import android.opengl.GLES20
import cn.xiaochuankeji.gift.view.GLTextureView
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ProcessingPipeline : GLTextureView.Renderer {

    var rendering = AtomicBoolean(false)

    var width = 0
    var height = 0

    val rootRenderers = mutableListOf<GLRenderer>()
    val filtersToDestory = mutableListOf<GLRenderer>()

    init {
        rendering.set(false)
    }

    fun addFilterToDestroy(renderer: GLRenderer) {
        synchronized(filtersToDestory) {
            if (!filtersToDestory.contains(renderer)) {
                filtersToDestory.add(renderer)
            }
        }
    }

    fun addRootRenderer(renderer: GLRenderer) {
        synchronized(rootRenderers) {
            if (!rootRenderers.contains(renderer)) {
                rootRenderers.add(renderer)
            }
        }
    }

    fun remoteRootRenderer(renderer: GLRenderer) {
        synchronized(rootRenderers) {
            rootRenderers.remove(renderer)
        }
    }

    fun isRendering(): Boolean {
        return rendering.get()
    }

    fun pauseRendering() {
        rendering.set(false)
    }

    fun startRender() {
        if (rootRenderers.size > 0) {
            rendering.set(true)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        if (isRendering()) {
            for (i in 0 until rootRenderers.size) {
                var rootRender: GLRenderer? = null
                synchronized(this) {
                    rootRender = rootRenderers[i]
                }
                rootRender?.onDrawFrame()
            }
        }
        synchronized(filtersToDestory) {
            for (renderer in filtersToDestory) {
                renderer.destroy()
            }
            filtersToDestory.clear()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_CONSTANT_ALPHA)
    }

    override fun onSurfaceDestroyed(gl: GL10?) {
        GLES20.glDisable(GLES20.GL_BLEND)
        synchronized(filtersToDestory) {
            for (renderer in filtersToDestory) {
                renderer.destroy()
            }
            filtersToDestory.clear()
        }
    }

}