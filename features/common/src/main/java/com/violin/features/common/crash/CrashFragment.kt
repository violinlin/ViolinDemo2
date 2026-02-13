package com.violin.features.common.crash

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.violin.base.act.BaseFragment
import com.violin.base.act.LogUtil
import com.violin.fretures.common.databinding.ActivityCrashBinding

class CrashFragment : BaseFragment<ActivityCrashBinding>() {
    companion object {
        val TAG = "CrashFragment"
        fun newInstance(): CrashFragment {
            return CrashFragment()
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityCrashBinding {

        return ActivityCrashBinding.inflate(inflater)
    }

    override fun initView() {
        binding.javaOomCrash.setOnClickListener {
            createJavaOOM()
        }
        binding.threadOomCrash.setOnClickListener {
            startCreatingThreads()
        }
        binding.bitmapOomCrash.setOnClickListener {
            bitmapOOM()
        }
        binding.btnTestAnr.setOnClickListener {
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
            val imageView = ImageView(binding.root.context)
            imageView.setImageBitmap(mBitmap)
        }

    }

    var mData: ByteArray? = null
    private fun createJavaOOM() {
        mData = ByteArray((Runtime.getRuntime().maxMemory().toInt() * 0.5).toInt())
        binding.root.postDelayed({
            val byte = ByteArray((Runtime.getRuntime().maxMemory().toInt() * 0.5).toInt())
            Log.d("CrashActivity", "MainActivity:" + getMemoryInfo())
        }, 1000)
    }

    fun getMemoryInfo(): String {
        val runtime = Runtime.getRuntime()
        val activityManager =
            binding.root.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
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
}