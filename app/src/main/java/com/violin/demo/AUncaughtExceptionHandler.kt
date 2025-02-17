package com.violin.demo

import android.app.ActivityManager
import android.content.Context
import android.util.Log

class AUncaughtExceptionHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        defaultHandler?.let {
            Thread.setDefaultUncaughtExceptionHandler(defaultHandler)
        }
        if (throwable is OutOfMemoryError) {
            Log.d("AUncaughtException", getMemoryInfo())
        }
        // 交给系统默认的异常处理器
        defaultHandler?.uncaughtException(thread, throwable)
    }

    fun getMemoryInfo(): String {
        val runtime = Runtime.getRuntime()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
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