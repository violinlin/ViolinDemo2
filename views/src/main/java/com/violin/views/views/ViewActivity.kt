package com.violin.views.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.violin.views.R
import org.libpag.PAGFile
import org.libpag.PAGView

class ViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val pagview = findViewById<PAGView>(R.id.pagview)
        pagview.composition = PAGFile.Load(assets, "piaopingdiban.pag")
        pagview.setRepeatCount(-1)
        pagview.play()
        val animlist = arrayOf(
            "anim_lucky_gift_level_3.pag",
            "anim_lucky_gift_level_4.pag",
            "bg03_bmp.pag",
            "bg04_bmp.pag",
            "half_bmp.pag"
        )
        pagview.setOnClickListener {
            count++
            pagview.composition = PAGFile.Load(assets, animlist[count % animlist.size])
            Log.d("pagView:", "duration" + pagview.composition.duration())
            pagview.setRepeatCount(-1)
            pagview.play()

        }

    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ViewActivity::class.java)
            context.startActivity(starter)
        }
    }
}