package cn.xiaochuankeji.gift.input

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import android.view.Surface
import cn.xiaochuankeji.gift.IEffectPlayer
import cn.xiaochuankeji.gift.OnRepeatListener
import cn.xiaochuankeji.gift.view.EffectGLTextureView
import cn.xiaochuankeji.gift.view.GLTextureView
import javax.microedition.khronos.opengles.GL10

/**
 * setRenderSize
 * setDataSource
 * prepare
 * start
 */
class VideoResourceInput(context: Context) : GLTextureOutputRenderer(),
    MediaPlayer.OnCompletionListener,
    IEffectPlayer,
    SurfaceTexture.OnFrameAvailableListener, MediaPlayer.OnPreparedListener {

    var repeatCount = 2
    protected var repeatNum = 0
    val mContext: Context = context
    var mPlayer: MediaPlayer? = null
    var mGLTextureView: GLTextureView? = null
    var mSurfaceTexture: SurfaceTexture? = null

    var isAsset = false
    var path: String? = null
    var completionListener: MediaPlayer.OnCompletionListener? = null
    var onRepeatListener: OnRepeatListener? = null
    var onPreparedListener: MediaPlayer.OnPreparedListener? = null


    private var matrixHandle = 0
    private val matrix = FloatArray(16)
    private var startWhenReady = false
    private var ready = false

    init {
        startWhenReady = false
        ready = false
    }

    companion object {
        private const val UNIFORM_CAM_MATRIX = "u_Matrix"
    }

    override fun setGLTextureView(glTextureView: GLTextureView) {
        this.mGLTextureView = glTextureView
    }

    fun setOnCompletionListener(l: MediaPlayer.OnCompletionListener?) {
        this.completionListener = l
    }

    override fun prepare() {
    }

    override fun setDataSource(path: String) {
        this.path = path
    }

    override fun startPlay() {
//        mPlayer?.prepareAsync()
        if (ready) {
            mPlayer?.start()
        } else {
            startWhenReady = true
        }
    }

    override fun stopPlay() {
        mPlayer?.pause()
    }

    override fun isPlayering(): Boolean {
        mPlayer ?: false
        return mPlayer!!.isPlaying
    }


    private fun initPlayer() {
        mPlayer?.apply {
            stopAndReleasePlayer()
        }
        try {
            mPlayer = MediaPlayer().apply {
                this.setOnCompletionListener(this@VideoResourceInput)
//                mPlayer.setAudioAttributes(AudioAttributes.USAGE_MEDIA)
            }
            mPlayer?.setOnPreparedListener(this@VideoResourceInput)
        } catch (e: Exception) {

        }
    }

    fun seekTo(seconds: Int) {
        mPlayer?.seekTo(seconds * 1000)
    }

    private fun stopAndReleasePlayer() {
        stopPlay()
        mPlayer?.apply {
            synchronized(this) {
                this.setOnErrorListener(null)
                this.setOnCompletionListener(null)
                this.setOnPreparedListener(null)
                this.setOnVideoSizeChangedListener(null)
                this.setSurface(null)
                this.stop()
                this.reset()
                this.release()
            }
        }
        mPlayer = null
        mSurfaceTexture?.apply {
            this.setOnFrameAvailableListener(null)
            this.release()
        }
        mSurfaceTexture = null
    }

    private fun bindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture_in)
    }

    override fun getVertexShader(): String {
        return """uniform mat4 $UNIFORM_CAM_MATRIX;
                attribute vec4 $ATTRIBUTE_POSITION;
                attribute vec2 $ATTRIBUTE_TEXCOORD;
                varying vec2 $VARYING_TEXCOORD;
                void main() {
                    vec4 texPos = $UNIFORM_CAM_MATRIX * vec4($ATTRIBUTE_TEXCOORD, 1, 1);
                    $VARYING_TEXCOORD = texPos.xy;
                    gl_Position = $ATTRIBUTE_POSITION;
                }
                """
    }

    override fun getFragmentShader(): String {
        return """#extension GL_OES_EGL_image_external : require
                precision mediump float;
                uniform samplerExternalOES $UNIFORM_TEXTURE0;
                varying vec2 $VARYING_TEXCOORD;
                void main() {
                   gl_FragColor = texture2D($UNIFORM_TEXTURE0, $VARYING_TEXCOORD);
                }
                """
    }

    override fun initWithGLContext() {
        ready = false
        initPlayer()
        if (isAsset) {
            val afd: AssetFileDescriptor = mContext.assets.openFd(path!!)
            mPlayer!!.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } else {
            mPlayer!!.setDataSource(path)
        }
        mPlayer!!.prepare()
        repeatNum = 0
        setRenderSize(mPlayer!!.videoWidth / 2, mPlayer!!.videoHeight)
        if (mGLTextureView is EffectGLTextureView) {
            (mGLTextureView as EffectGLTextureView)?.setRenderSize(getWidth(), getHeight())
        }
        super.initWithGLContext()

        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0])
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MIN_FILTER,
            GL10.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MAG_FILTER,
            GL10.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_S,
            GL10.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_T,
            GL10.GL_CLAMP_TO_EDGE
        )
        texture_in = textures[0]

        mSurfaceTexture = SurfaceTexture(texture_in)
        mSurfaceTexture!!.setOnFrameAvailableListener(this)

        val surface = Surface(mSurfaceTexture!!)
        mPlayer?.setSurface(surface)
        ready = true
        if (startWhenReady) {
            mPlayer?.start()
        }
    }

    override fun initShaderHandles() {
        super.initShaderHandles()
        matrixHandle = GLES20.glGetUniformLocation(
            programHandle,
            UNIFORM_CAM_MATRIX
        )
    }

    override fun passShaderValues() {
        renderVertices!!.position(0)
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 8, renderVertices)
        GLES20.glEnableVertexAttribArray(positionHandle)
        textureVertices[curRotation]!!.position(0)
        GLES20.glVertexAttribPointer(
            texCoordHandle,
            2,
            GLES20.GL_FLOAT,
            false,
            8,
            textureVertices[curRotation]
        )
        GLES20.glEnableVertexAttribArray(texCoordHandle)

        bindTexture()
        GLES20.glUniform1i(textureHandle, 0)
        mSurfaceTexture?.getTransformMatrix(matrix)
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, matrix, 0)
    }

    override fun drawFrame() {
        mSurfaceTexture!!.updateTexImage()
        super.drawFrame()
    }

    override fun onDrawFrame() {
        try {
            super.onDrawFrame()
        } catch (e: Exception) {
            completionListener?.onCompletion(null)
        }
    }

    override fun destroy() {
        super.destroy()
        Log.i("GLTextureView", "ondestroy")
        ready = false
        startWhenReady = false
        stopAndReleasePlayer()
        if (matrixHandle != 0) {
            GLES20.glDeleteProgram(matrixHandle)
            matrixHandle = 0
        }
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
//        updateSurface.set(true)
        markAsDirty()
        mGLTextureView?.requestRender()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (repeatCount == -1 || ++repeatNum < repeatCount) {
            mp?.start()
            onRepeatListener?.onRepeat()
        } else {
            completionListener?.onCompletion(mp)
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        onPreparedListener?.onPrepared(mp)
    }

}