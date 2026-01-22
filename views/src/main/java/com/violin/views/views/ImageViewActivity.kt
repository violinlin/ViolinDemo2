package com.violin.views.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.violin.views.R
import com.violin.views.databinding.ActivityImageViewBinding
import com.violin.views.databinding.ActivityViewBinding

class ImageViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    private fun initView() {
//        720 1440
        val url = "http://me-media-gateway.test03.youyisia.com/img/png/id/1279378360"
        val ivScaleType = ImageView.ScaleType.CENTER_CROP
        binding.iv11.scaleType = ivScaleType
        binding.iv21.scaleType = ivScaleType
        Glide.with(binding.iv11).load(url).into(binding.iv11)

        Glide.with(binding.iv21).load(url).into(binding.iv21)

    }


    companion object {
        fun start(context: Context) {
            val starter = Intent(context, ImageViewActivity::class.java)
            context.startActivity(starter)
        }
    }

}