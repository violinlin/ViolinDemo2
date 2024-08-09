package cn.xiaochuankeji

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.xiaochuankeji.gift.OnRepeatListener
import cn.xiaochuankeji.gift.player.EffectPlayer
import com.violin.base.act.FileUtils
import com.violin.fretures.livevideogift.R
import java.io.File

class VideoActivity : AppCompatActivity() {
    lateinit var fl_layout: ViewGroup
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

    private fun initView() {
        fl_layout = findViewById(R.id.fl_layout)
        findViewById<Button>(R.id.btn_start_play).setOnClickListener {
            val targetFile = File(this.getExternalFilesDir(null), "anima.mp4")
            if (!targetFile.exists()) {
                FileUtils.copyAssetFileToTarget(this, "anima.mp4", targetFile.absolutePath)
            }
            startPlay(targetFile.absolutePath)
        }
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
                    override fun onPlayCompletion(mp: MediaPlayer?) {
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

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, VideoActivity::class.java)
            context.startActivity(starter)
        }
    }
}