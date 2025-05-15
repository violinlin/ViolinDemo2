package com.violin.features.common.crash

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.violin.base.act.LogUtil
import com.violin.fretures.common.R

class CrashActivity : AppCompatActivity() {
    val TAG = "CrashActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    private fun initView() {
        findViewById<View>(R.id.java_oom_crash).setOnClickListener {
            createJavaOOM()
        }

        findViewById<View>(R.id.thread_oom_crash).setOnClickListener {
            startCreatingThreads()

        }

        findViewById<View>(R.id.bitmap_oom_crash).setOnClickListener {
            bitmapOOM()
        }
        findViewById<View>(R.id.btn_test_anr).setOnClickListener {
            testThreadAnr()
        }
    }

    private fun testThreadAnr() {
        try {
            var number = 0
            while (number++ < 5) {
                LogUtil.e(TAG, "主线程睡眠导致的ANR:次数$number/5")
                try {
                    Thread.sleep(5000L)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    LogUtil.e(TAG, "异常信息为:" + e.message)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            LogUtil.e(TAG, "异常信息为:" + e.message)
        }
    }

    var threadCount = 0
    private fun startCreatingThreads() {

        Thread {
            while (true) {
                try {
                    createAndHoldThread()
                    threadCount++
                    Log.d("CrashActivity", "Thread created: $threadCount")
                } catch (e: Throwable) {
                    Log.e("CrashActivity", "Error: ", e)
                    break
                }
            }
        }.start()
    }

    private fun createAndHoldThread() {
//        try {
        Thread(null, kotlinx.coroutines.Runnable {

            // 让线程保持活跃（不退出），模拟线程泄漏
            while (true) {
                try {
                    Thread.sleep(1000) // 防止 CPU 忙等待
                } catch (e: InterruptedException) {
                    break
                }
            }

        }, "name", 1024 * 1024 * 500).start()
        Log.d("CrashActivity", "MainActivity:" + getMemoryInfo())
//        } catch (e:Throwable) {
//            e.printStackTrace()
//        }

    }

    private fun bitmapOOM() {
        for (i in 0..100) {
            val size = 1024 * 5 * 1024
            val mBitmap = Bitmap.createBitmap(
                size.toInt(),
                size.toInt(),
                Bitmap.Config.ARGB_8888
            )
            val imageView = ImageView(this)
            imageView.setImageBitmap(mBitmap)
        }

    }

    var mData: ByteArray? = null
    private fun createJavaOOM() {
        mData = ByteArray((Runtime.getRuntime().maxMemory().toInt() * 0.5).toInt())
        window.decorView.postDelayed({
            val byte = ByteArray((Runtime.getRuntime().maxMemory().toInt() * 0.5).toInt())
            Log.d("CrashActivity", "MainActivity:" + getMemoryInfo())
        }, 1000)
    }

    fun getMemoryInfo(): String {
        val runtime = Runtime.getRuntime()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        return """当前可用内存: ${memoryInfo.availMem / 1024 / 1024} MB
        | 是否处于低内存状态: ${memoryInfo.lowMemory}
        | 最大堆内存: ${runtime.maxMemory() / 1024 / 1024} MB
        | 已分配内存: ${(runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024} MB
        | 剩余堆内存: ${runtime.freeMemory() / 1024 / 1024} MB
        | 当前线程数:${Thread.activeCount()}
    """.trimMargin()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, CrashActivity::class.java)
            context.startActivity(starter)
        }
    }
}