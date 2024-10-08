package cn.xiaochuankeji.gift.input

import android.opengl.GLES20
import cn.xiaochuankeji.gift.GLRenderer


open class GLTextureOutputRenderer : GLRenderer() {
    protected var frameBuffer: IntArray? = null
    protected var texture_out: IntArray? = null
    protected var depthRenderBuffer: IntArray? = null

    private var targets: MutableList<GLTextureInputRenderer>? = null
    private val listLock = Any()

    private var dirty = false

    /**
     * Creates a GLTextureOutputRenderer which initially has an empty list of targets.
     */
    init {
        targets = ArrayList<GLTextureInputRenderer>()
    }

    /**
     * Adds the given target to the list of targets that this renderer sends its output to.
     * @param target
     * The target which should be added to the list of targets that this renderer sends its output to.
     */
    @Synchronized
    fun addTarget(target: GLTextureInputRenderer) {
        synchronized(listLock) { targets!!.add(target) }
    }

    /* (non-Javadoc)
	 * @see project.android.imageprocessing.GLRenderer#destroy()
	 */
    override fun destroy() {
        super.destroy()
        if (frameBuffer != null) {
            GLES20.glDeleteFramebuffers(1, frameBuffer, 0)
            frameBuffer = null
        }
        if (texture_out != null) {
            GLES20.glDeleteTextures(1, texture_out, 0)
            texture_out = null
        }
        if (depthRenderBuffer != null) {
            GLES20.glDeleteRenderbuffers(1, depthRenderBuffer, 0)
            depthRenderBuffer = null
        }
    }

    override fun drawFrame() {
        if (frameBuffer == null) {
            if (getWidth() != 0 && getHeight() != 0) {
                initFBO()
            } else {
                return
            }
        }
        var newData = false
        if (dirty) {
            newData = true
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer!![0])
            super.drawFrame()
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        }
        synchronized(listLock) {
            for (target in targets!!) {
                target.newTextureReady(texture_out!![0], this, newData)
            }
        }
    }

    /**
     * Returns the object used to lock the target list.  Iterating over or changing the target list
     * should be done in a synchronized block that is locked using the object return.
     * @return lock
     * the object which is used to lock the target list
     */
    fun getLockObject(): Any? {
        return listLock
    }

    /**
     * Returns a list of all the targets that this renderer should send its output to.  Iterating over or changing this
     * list should be done in a synchronized block, locked using the object returned from getLockObject().
     * @return targets
     */
    fun getTargets(): List<GLTextureInputRenderer>? {
        return targets
    }

    override fun handleSizeChange() {
        super.handleSizeChange()
        initFBO()
    }

    private fun getMaxWidth(): Int {
        val maxTextureSize = IntArray(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0)
        val maxSize = maxTextureSize[0]

        val textureWidth = Math.min(getWidth(), maxSize)
        return textureWidth
    }

    private fun getMaxHeight(): Int {
        val maxTextureSize = IntArray(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0)
        val maxSize = maxTextureSize[0]

        val textureHeight = Math.min(getHeight(), maxSize)
        return textureHeight
    }



    private fun initFBO() {
        if (frameBuffer != null) {
            GLES20.glDeleteFramebuffers(1, frameBuffer, 0)
            frameBuffer = null
        }
        if (texture_out != null) {
            GLES20.glDeleteTextures(1, texture_out, 0)
            texture_out = null
        }
        if (depthRenderBuffer != null) {
            GLES20.glDeleteRenderbuffers(1, depthRenderBuffer, 0)
            depthRenderBuffer = null
        }
        frameBuffer = IntArray(1)
        texture_out = IntArray(1)
        depthRenderBuffer = IntArray(1)
        GLES20.glGenFramebuffers(1, frameBuffer, 0)
        GLES20.glGenRenderbuffers(1, depthRenderBuffer, 0)
        GLES20.glGenTextures(1, texture_out, 0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer!![0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_out!![0])
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            getMaxWidth(),
            getMaxHeight(),
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER,
            GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D,
            texture_out!![0],
            0
        )
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthRenderBuffer!![0])
        GLES20.glRenderbufferStorage(
            GLES20.GL_RENDERBUFFER,
            GLES20.GL_DEPTH_COMPONENT16,
            getWidth(),
            getHeight()
        )
        GLES20.glFramebufferRenderbuffer(
            GLES20.GL_FRAMEBUFFER,
            GLES20.GL_DEPTH_ATTACHMENT,
            GLES20.GL_RENDERBUFFER,
            depthRenderBuffer!![0]
        )
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            throw RuntimeException(this.toString() + ": Failed to set up render buffer with status " + status + " and error " + GLES20.glGetError())
        }
    }

    /**
     * Removes the given target from the list of targets that this renderer sends its output to.
     * @param target
     * The target which should be removed from the list of targets that this renderer sends its output to.
     */
    fun removeTarget(target: GLTextureInputRenderer) {
        synchronized(listLock) { targets!!.remove(target) }
    }

    fun markAsDirty() {
        dirty = true
    }
}