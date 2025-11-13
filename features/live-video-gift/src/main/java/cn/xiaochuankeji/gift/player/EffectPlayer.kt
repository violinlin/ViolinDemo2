package cn.xiaochuankeji.gift.player

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import cn.xiaochuankeji.gift.OnRepeatListener
import cn.xiaochuankeji.gift.ProcessingPipeline
import cn.xiaochuankeji.gift.filter.AlphaFilter
import cn.xiaochuankeji.gift.filter.BasicFilter
import cn.xiaochuankeji.gift.input.VideoResourceInput
import cn.xiaochuankeji.gift.output.ScreenEndpoint
import cn.xiaochuankeji.gift.view.EffectGLTextureView
import cn.xiaochuankeji.gift.view.PlayerContainerView

class EffectPlayer constructor(context: Context) : MediaPlayer.OnCompletionListener,
    OnRepeatListener, MediaPlayer.OnPreparedListener {
    public interface OnPlayCompletionListener {
        fun onPlayCompletion(mp: MediaPlayer?)
    }

    var handler = Handler(Looper.getMainLooper())
    var context = context
    val playerContainerView: PlayerContainerView = PlayerContainerView(context)

    val pipeline: ProcessingPipeline = ProcessingPipeline()

    val videoInput: VideoResourceInput = VideoResourceInput(context)
    val alphaFilter: BasicFilter = AlphaFilter()
    val screenEndpoint: ScreenEndpoint

    var listener: OnPlayCompletionListener? = null
    var onRepeatListener: OnRepeatListener? = null
    var onPreparedListener: MediaPlayer.OnPreparedListener? = null
    var starting = false

    init {
        screenEndpoint = ScreenEndpoint(pipeline)
        videoInput.addTarget(alphaFilter)
        alphaFilter.addTarget(screenEndpoint)
        pipeline.addRootRenderer(videoInput)

        videoInput.setOnCompletionListener(this)
        videoInput.onRepeatListener = this
        videoInput.onPreparedListener = this
    }

    fun setRepeatCount(count: Int) {
        videoInput.repeatCount = count
    }

    fun setDataSource(path: String) {
        videoInput.setDataSource(path)
    }

    fun setAsset(path: String) {
        videoInput.setDataSource(path)
        videoInput.isAsset = true
    }

    fun setScaleType(scaleType: Int) {
        playerContainerView.setScaleType(scaleType)
    }

    fun setGravity(gravity: Int) {
        playerContainerView.setGravity(gravity)
    }

    fun isStarting(): Boolean {
        return starting
    }

    fun start(parent: ViewGroup) {
        if (starting) {
            stop()
        }
        starting = true
        playerContainerView.reset()
        val playerView = playerContainerView.getGLTextureView()
        playerContainerView.setPipeline(pipeline)
        parent.addView(
            playerContainerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        videoInput.setGLTextureView(playerView)
        playerContainerView.attach()
        pipeline.startRender()
        videoInput.startPlay()
    }

    fun stop() {
        if (!starting) {
            return
        }
        starting = false
        pipeline.addFilterToDestroy(videoInput)
        pipeline.addFilterToDestroy(alphaFilter)
        pipeline.addFilterToDestroy(screenEndpoint)
        //隐藏 触发消失
        playerContainerView.detach()
        if (playerContainerView.parent != null) {
            (playerContainerView.parent as ViewGroup).removeView(playerContainerView)
        }
    }

    fun pause() {
        videoInput.stopPlay()
    }

    fun startPlay() {
        videoInput.startPlay()
    }
    companion object {
        const val FIT_CENTER = EffectGLTextureView.FIT_CENTER
        const val CENTER_CROP = EffectGLTextureView.CENTER_CROP
        const val HEIGHT_CROP = EffectGLTextureView.HEIGHT_CROP

        fun play(
            context: Context,
            parent: ViewGroup,
            path: String,
            repeatCount: Int,
            l: OnPlayCompletionListener
        ): EffectPlayer {
            return play(
                context,
                parent,
                path,
                FIT_CENTER,
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
                repeatCount,
                l
            )
        }


        fun play(
            context: Context,
            parent: ViewGroup,
            path: String,
            scaleType: Int,
            gravity: Int,
            repeatCount: Int,
            l: OnPlayCompletionListener? = null,
            onRepeatListener: OnRepeatListener? = null,
            onPreparedListener: MediaPlayer.OnPreparedListener? = null
        ): EffectPlayer {
            val player = EffectPlayer(context)
            player.setDataSource(path)
            player.setRepeatCount(repeatCount)
            player.setScaleType(scaleType)
            player.setGravity(gravity)
            player.listener = l
            player.onRepeatListener = onRepeatListener
            player.onPreparedListener = onPreparedListener
            player.start(parent)
            return player
        }

        fun readMediaInfo(path: String?): MediaInfo {
            path ?: MediaInfo()
            return try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(path)
                var duration =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
//                val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
//                val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                var mediaDuration = duration?.toLong() ?: 0
                MediaInfo(mediaDuration)
            } catch (e: Exception) {
                MediaInfo()
            }
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        //停掉pipeline
        runOnUi(Runnable {
            stop()
            listener?.onPlayCompletion(mp)
        })
    }

    private fun runOnUi(runnable: Runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run()
        } else {
            handler.post(runnable)
        }
    }

    override fun onRepeat() {
        onRepeatListener?.onRepeat()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        onPreparedListener?.onPrepared(mp)
    }
}