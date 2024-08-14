package com.violin.views.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.violin.views.R
import org.libpag.PAGFile
import org.libpag.PAGView

class ViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val pagview = findViewById<PAGView>(R.id.pagview)
        pagview.composition = PAGFile.Load(assets, "anima_lucky_gift_bg.pag")
        pagview.setRepeatCount(-1)
        pagview.play()

    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ViewActivity::class.java)
            context.startActivity(starter)
        }
    }
}