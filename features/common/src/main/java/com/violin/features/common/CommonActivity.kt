package com.violin.features.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.violin.features.common.service.ViolinNormalService
import com.violin.features.common.service.ViolinService
import com.violin.fretures.common.R
import com.violin.fretures.common.databinding.ActivityCommonBinding
import kotlin.concurrent.thread

class CommonActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityCommonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityCommonBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        findViewById<View>(R.id.btn_share).setOnClickListener {
            val hasWritePermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            thread {
                ShareUtil.share(this)
            }
//            if (hasWritePermission) {
//                thread {
//                    ShareUtil.share(this)
//                }
//            } else {
//                requestWritePermission()
//            }

        }
        mBinding.serviceStart.setOnClickListener {
            ViolinService.start(this)
            ViolinNormalService.start(this)

        }
        mBinding.serviceStartForground.setOnClickListener {
            ViolinService.startForground(this)
        }
        mBinding.serviceStop.setOnClickListener {
            ViolinService.stop(this)
        }

    }

    private fun requestWritePermission() {

        val permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        ActivityCompat.requestPermissions(this, permissions, 1)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, CommonActivity::class.java)
            context.startActivity(starter)
        }
    }
}