package cn.xiaochuankeji

import android.content.Context
import android.graphics.SurfaceTexture
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.Surface
import android.view.TextureView
import android.widget.FrameLayout
import cn.xiaochuankeji.gift.view.EffectGLTextureView
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class VideoPlayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), TextureView.SurfaceTextureListener {

    private var url: String? = null
    private var headers: Map<String, String>? = null
    private var mediaPlayer: IMediaPlayer? = null   //播放器
    private var container: FrameLayout? = null      //播放器根目录
    private var textureView: EffectGLTextureView? = null   //视频显示视图
    private var surfaceTexture: SurfaceTexture? = null

    //    private var playerType: Int = PlayerTypes.TYPE_IJK
    private var surface: Surface? = null

    init {
        initView()
    }

    /**
     * 设置播放器类型
     *
     * @param playerType IjkPlayer or MediaPlayer.
     */
//    fun setPlayerType(playerType: Int) {
//        this.playerType = playerType
//    }

    fun setUp(url: String, headers: Map<String, String>?) {
        this.url = url
        this.headers = headers
    }

    fun start() {
        openMediaPlayer()
        mediaPlayer?.start()
    }

    private fun initView() {
        container = FrameLayout(context)
        this.removeView(container)
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        this.addView(container, params)

        initTextureView()
        initMediaPlayer()
    }

    private fun initTextureView() {
        if (textureView == null) {
            textureView = EffectGLTextureView(context)
            textureView?.surfaceTextureListener = this
        }
        container?.removeView(textureView)
        val params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        container?.addView(textureView, 0, params)
    }

    private fun initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = IjkMediaPlayer()
        }

        mediaPlayer?.setOnVideoSizeChangedListener { player, w, h, sar_num, sar_den ->
            Log.d("======", "setOnVideoSizeChangedListener")
        }

        mediaPlayer?.setOnErrorListener { player, what, extra ->
            Log.d("======", "setOnErrorListener")
            true
        }

        mediaPlayer?.setOnCompletionListener {
            Log.d("======", "setOnCompletionListener")
        }

        mediaPlayer?.setOnPreparedListener {
            Log.d("======", "setOnPreparedListener")
        }

        mediaPlayer?.setOnInfoListener { player, what, extra ->
            Log.d("======", "setOnInfoListener")
            true
        }

        mediaPlayer?.setOnBufferingUpdateListener { player, percent ->
            Log.d("======", "setOnBufferingUpdateListener")

        }

//        openMediaPlayer()
    }

    private fun openMediaPlayer() {
//        mediaPlayer?.setDataSource(context, Uri.parse(url), headers)
        mediaPlayer?.dataSource = url
        mediaPlayer?.prepareAsync()
    }


    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {

    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {

    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
        return p0 == null
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
        if (surfaceTexture == null) {
            surfaceTexture = p0
            if (surface == null) {
                surface = Surface(surfaceTexture)
            }
            mediaPlayer?.setSurface(surface)
        } else {
            surfaceTexture?.let {
                textureView?.setSurfaceTexture(it)
            }
        }
    }


}