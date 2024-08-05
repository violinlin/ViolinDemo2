package com.violin.features.common

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.violin.base.act.FileUtils
import com.violin.base.act.LogUtil
import java.io.File

object ShareUtil {
    val TAG = "ShareUtil"
    fun share(context: Context) {

        // sd卡报名目录
//        val imageFile =
//            File(context.getExternalFilesDir("my_external_files"), "image.jpg")
        // SD卡根目录
//        val imageFile = File(Environment.getExternalStorageDirectory(),"my_external")
        // 沙盒目录
        val imageFile = File(context.getFilesDir(), "my_internal_files/image.jpg")
        LogUtil.logD(TAG, "imageFilepath = ${imageFile.absolutePath}")
        if (!imageFile.exists()) {
            FileUtils.copyAssetFileToTarget(context, "share.png", imageFile.absolutePath)
        }

        val imageUri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
        LogUtil.logD(TAG, "imageUri = $imageUri")

        try {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/jpeg"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}