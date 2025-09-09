package com.violin.features.common.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.violin.base.act.LogUtil
import com.violin.fretures.common.R

class ViolinNormalService : Service() {
    val TAG = "ViolinNormalService"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.d(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action.let {
            when (it) {
                ACTION_START -> {

                }
                ACTION_START_Foreground -> {
                    // 创建前台通知
                    val notification = createNotification(baseContext)
                    // 必须调用，否则会报错
                    startForeground(1, notification)
                }

                ACTION_STOP -> {
                    stopSelf()
                }
            }
            LogUtil.d(TAG, "onStartCommand:action=${it}")
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun createNotification(context: Context): Notification {
        val channelId = "my_foreground_service"
        val channelName = "My Foreground Service"

        // Android 8.0+ 必须创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        // 跳转到应用设置
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, channelId)
//            .setContentTitle("服务正在运行")
//            .setContentText("保持任务不中断")
            .setContentIntent(pendingIntent)
//            .setSmallIcon(com.tencent.sqlitelint.R.drawable.ic_launcher)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d(TAG, "onDestroy")
    }

    companion object {
        private const val ACTION_START = "action_start_server"
        private const val ACTION_START_Foreground = "action_start_server_foreground"
        private const val ACTION_STOP = "action_stop_server"
        fun start(context: Context) {
            val intent = Intent(context, ViolinNormalService::class.java)
            intent.setAction(ACTION_START)
            context.startService(intent)
        }



        fun stop(context: Context) {
            val intent = Intent(context, ViolinNormalService::class.java)
            intent.setAction(ACTION_STOP)
            context.startService(intent)
        }
    }
}