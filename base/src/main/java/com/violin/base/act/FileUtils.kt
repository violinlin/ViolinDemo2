package com.violin.base.act

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {

    fun copyAssetFileToTarget(context: Context, assetFileName: String, targetFilePath: String) {
        try {
            // 获取 assets 文件夹中的文件
            val assetManager = context.assets
            val inputStream: InputStream = assetManager.open(assetFileName)

            // 创建目标文件
            val targetFile = File(targetFilePath)
            if (!targetFile.exists()) {
                targetFile.parentFile?.mkdirs()
                targetFile.createNewFile()
            }

            // 打开目标文件的输出流
            val outputStream = FileOutputStream(targetFile)

            // 缓冲区
            val buffer = ByteArray(1024)
            var length: Int

            // 读取 assets 文件内容并写入目标文件
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            // 关闭流
            outputStream.flush()
            outputStream.close()
            inputStream.close()

            println("文件复制成功：$assetFileName -> $targetFilePath")
        } catch (e: Exception) {
            e.printStackTrace()
            println("文件复制失败：$e")
        }
    }


}