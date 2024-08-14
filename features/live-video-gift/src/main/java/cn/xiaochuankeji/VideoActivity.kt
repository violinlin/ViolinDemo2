package cn.xiaochuankeji

import android.content.Context
import android.content.Intent
import android.media.MediaCodec
import android.media.MediaCodecList
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Range
import android.view.Gravity
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.Button
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import cn.xiaochuankeji.gift.OnRepeatListener
import cn.xiaochuankeji.gift.player.EffectPlayer
import com.violin.base.act.FileUtils
import com.violin.base.act.LogUtil
import com.violin.fretures.livevideogift.R
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.File
import java.io.IOException


class VideoActivity : AppCompatActivity() {
    lateinit var fl_layout: ViewGroup
    lateinit var surfaceView: SurfaceView
    lateinit var video_player: VideoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()

    }

    fun getSupportedVideoMimeTypes(): Set<String> {
        val supportedMimeTypes = mutableSetOf<String>()

        // 获取设备上的所有编解码器
        val codecList = MediaCodecList(MediaCodecList.ALL_CODECS)
        val codecInfos = codecList.codecInfos

        // 遍历所有的编解码器信息
        for (codecInfo in codecInfos) {
            // 只关心解码器
            if (!codecInfo.isEncoder) {
                val supportedTypes = codecInfo.supportedTypes
                for (type in supportedTypes) {
                    // 只关心视频 MIME 类型
                    if (type.startsWith("video/")) {
                        if (type !in supportedMimeTypes) {
                            supportedMimeTypes.add(type)
                        }
                    }
                }
            }
        }

        return supportedMimeTypes
    }

    fun getMaxSupportedBitrate(mimeType: String) {
        try {
            val codecList = MediaCodecList(MediaCodecList.ALL_CODECS)
            val codecInfos = codecList.codecInfos

            for (codecInfo in codecInfos) {
                if (!codecInfo.isEncoder) {
                    val capabilities = codecInfo.getCapabilitiesForType(mimeType)
                    if (capabilities != null) {
                        val videoCapabilities = capabilities.videoCapabilities
                        if (videoCapabilities != null) {
                            val bitrateRange: Range<Int> = videoCapabilities.bitrateRange
                            val maxBitrate = bitrateRange.upper
                            val minBinder = bitrateRange.lower
//                            60000000
                            println("Codec: ${codecInfo.name}, Max Bitrate: $maxBitrate lower Bitrate:$minBinder")
                        }
                    }
                }
            }
        } catch (e: Exception) {
//            e.printStackTrace()
        }

    }

    fun getVideoMimeType(videoFilePath: String): String? {
        val mediaExtractor = MediaExtractor()
        var mimeType: String? = null

        try {
            // 设置视频文件路径
            mediaExtractor.setDataSource(videoFilePath)

            // 遍历所有的轨道，找到视频轨道
            for (i in 0 until mediaExtractor.trackCount) {
                val format: MediaFormat = mediaExtractor.getTrackFormat(i)
                val trackMimeType: String? = format.getString(MediaFormat.KEY_MIME)

                // 检查 MIME 类型是否为视频
                if (trackMimeType != null && trackMimeType.startsWith("video/")) {
                    mimeType = trackMimeType
                    val bitrate = if (format.containsKey(MediaFormat.KEY_BIT_RATE)) {
                        format.getInteger(MediaFormat.KEY_BIT_RATE)
                    } else {
                        0
                    }
                    LogUtil.d("VideoActivity", "video bitrate:${bitrate} $videoFilePath")
                    break
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            mediaExtractor.release()
        }

        return mimeType
    }

    private fun initView() {
        fl_layout = findViewById(R.id.fl_layout)
        surfaceView = findViewById(R.id.surface_view)
        findViewById<Button>(R.id.btn_start_play).setOnClickListener {
//            for (i in arrayOf("birthday.mp4", "birthday3.mp4", "leftColorRightAlpha.mp4")) {
//                val targetFile = File(this.getExternalFilesDir(null), i)
////            val targetFile = File(this.getExternalFilesDir(null), "leftColorRightAlpha_1.mp4")
//                if (!targetFile.exists()) {
//                    FileUtils.copyAssetFileToTarget(
//                        this,
//                        i,
//                        targetFile.absolutePath
//                    )
//                }
//                val mimeType = getVideoMimeType(targetFile.absolutePath)
//                LogUtil.d("VideoActivity", "mimeType:${mimeType} $i")
//                mimeType?.let {
//                    getMaxSupportedBitrate(it)
//                }
//                val supportedVideoMimeTypes = getSupportedVideoMimeTypes()
//                for (i in supportedVideoMimeTypes) {
//                    LogUtil.d("VideoActivity", "supportedVideoMimeTypes:${mimeType}")
//                }
//            }


            val fileName = "birthday.mp4"
            val targetFile = File(this.getExternalFilesDir(null), fileName)
            if (!targetFile.exists()) {
                FileUtils.copyAssetFileToTarget(
                    this,
                    fileName,
                    targetFile.absolutePath
                )
            }
            startPlay(targetFile.absolutePath)
//            exoPlayer3(targetFile.absolutePath)
//            ijkPlayer(targetFile.absolutePath)
//            initVideoView(targetFile.absolutePath)

        }
        try {
            IjkMediaPlayer.loadLibrariesOnce(null)
            IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initVideoView(path: String) {
        if (!::video_player.isInitialized) {
            video_player = findViewById(R.id.video_player)
            video_player.setUp(path, mapOf())
        }
        video_player.start()
    }


    var player: EffectPlayer? = null
    private fun startPlay(path: String) {

        if (player == null) {
            player = EffectPlayer.Companion.play(
                this,
                fl_layout,
                path,
                EffectPlayer.CENTER_CROP,
                Gravity.CENTER,
                1,
                object : EffectPlayer.OnPlayCompletionListener {
                    override fun onPlayCompletion(mp: IMediaPlayer?) {
                    }
                }, object : OnRepeatListener {
                    override fun onRepeat() {
                    }

                }
            )
        } else {
            player?.setRepeatCount(1)
            player?.setGravity(Gravity.CENTER)
            player?.setScaleType(EffectPlayer.CENTER_CROP)
            player?.setDataSource(path)
            player?.start(fl_layout)
        }

    }

    lateinit var player_view: PlayerView

    @OptIn(UnstableApi::class)
    private fun exoPlayer3(path: String) {
        if (!::player_view.isInitialized) {
            player_view = findViewById(R.id.player_view)
            val factory = DefaultRenderersFactory(this)
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
            MediaCodec.createByCodecName("c2.android.avc.decoder")
            val exoPlayer = ExoPlayer.Builder(this, factory)
                .build()


            player_view.player = exoPlayer
            player_view.player?.run {
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                    }

                })
            }
        }
        player_view.player?.run {
            stop()
            setMediaItem(MediaItem.fromUri(path))
            prepare()
        }


    }

    var ijkPlayer: IjkMediaPlayer? = null
    private fun ijkPlayer(videoPath: String) {
        if (ijkPlayer == null) {

            ijkPlayer = IjkMediaPlayer()
            ijkPlayer?.setDisplay(surfaceView.holder)
            ijkPlayer?.dataSource = videoPath;
            ijkPlayer?.prepareAsync();
        }
// ijkMediaPlayer.setDataSource("http://xxx.com/xxx.mp4");

        ijkPlayer?.start();
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, VideoActivity::class.java)
            context.startActivity(starter)
        }
    }
}