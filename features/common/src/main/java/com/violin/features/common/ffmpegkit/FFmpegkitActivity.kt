package com.violin.features.common.ffmpegkit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.violin.base.act.FileUtils
import com.violin.fretures.common.R
import com.violin.fretures.common.databinding.ActivityFfmpegkitBinding
import com.zuiyou.media.ffmpegkitwrapper.FFmpegKitUtil
import java.io.File

class FFmpegkitActivity : AppCompatActivity() {
    lateinit var activityFfmpegkitBinding: ActivityFfmpegkitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityFfmpegkitBinding = ActivityFfmpegkitBinding.inflate(layoutInflater, null, false)
        setContentView(activityFfmpegkitBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    private fun initView() {
        activityFfmpegkitBinding.btnVideoInfo.setOnClickListener {
            val videoName = "sanxing.mp4"
            val mediaFile = File(this.getFilesDir(), videoName)
            if (mediaFile.exists()) {
                mediaFile.delete()
            }
            val mediaPath = mediaFile.absolutePath
            val sb = StringBuilder()

            FileUtils.copyAssetFileToTarget(this, videoName, mediaPath)
            sb.append(videoName)
                .append("width:" + FFmpegKitUtil.getVideoWidth(mediaPath))
                .append("height:" + FFmpegKitUtil.getVideoHeight(mediaPath))
                .append("duration:" + FFmpegKitUtil.getMediaDuration(mediaPath))
                .append("rotate:" + FFmpegKitUtil.getVideoRotation(mediaPath))
                .append("videosize:" + FFmpegKitUtil.getVideoSize(mediaPath))

            sb.append("\n")
            val audioName = "audio.wav"
            val audioPath = File(this.getFilesDir(), audioName).absolutePath
            FileUtils.copyAssetFileToTarget(this, audioName, audioPath)

            sb.append(audioName)
                .append("duration:" +  FFmpegKitUtil.getMediaDuration(audioPath))

            activityFfmpegkitBinding.text.text = sb.toString()
        }
        activityFfmpegkitBinding.btnVideoBitmap.setOnClickListener {
            val videoName = "sanxing.mp4"
            val mediaPath = File(this.getFilesDir(), videoName).absolutePath
            FileUtils.copyAssetFileToTarget(this, videoName, mediaPath)
            val bitmap = FFmpegKitUtil.getVideoThumbnail(mediaPath, 0, this)
            bitmap?.let {
                activityFfmpegkitBinding.ivVideo.setImageBitmap(it)
            }

        }
        activityFfmpegkitBinding.btnAudioTransform.setOnClickListener {
            val audioName = "audio.wav"
            val audioPath = File(this.getExternalFilesDir(""), audioName).absolutePath

            val aacPath =
                File(this.getExternalFilesDir(""), audioName.replace("wav", "aac")).absolutePath
            FileUtils.copyAssetFileToTarget(this, audioName, audioPath)
            FFmpegKitUtil.wavToAac(audioPath, aacPath)

        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, FFmpegkitActivity::class.java)
            context.startActivity(starter)
        }
    }
}