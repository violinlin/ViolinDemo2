package com.zuiyou.media.ffmpegkitwrapper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFprobeKit
import com.violin.base.act.LogUtil
import java.io.File

object FFmpegKitUtil {
    private const val TAG = "FFmpegUtils"

    /**
     * 获取媒体文件时长（单位：毫秒）
     * 支持音频/视频
     */
    fun getMediaDuration(filePath: String): Long {
        val command = "-i \"$filePath\" -show_entries format=duration -v quiet -of csv=p=0"
        val session = FFprobeKit.execute(command)
        val output = session.output?.trim()
        return try {
            val seconds = output?.toDoubleOrNull() ?: 0.0
            (seconds * 1000).toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

    fun getVideoSize(filePath: String): Pair<Int, Int> {
        val command =
            "-i \"$filePath\" -v error -select_streams v:0 -show_entries stream=width,height -of csv=s=x:p=0"
        val session = FFprobeKit.execute(command)
        val output = session.output?.trim()
        LogUtil.d(TAG, "getVideoSize output=$output")

        return try {
            val parts = output?.split("x") ?: emptyList()
            if (parts.size == 2) {
                Pair(parts[0].toInt(), parts[1].toInt())
            } else {
                Pair(0, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(0, 0)
        }
    }


    /**
     * 获取视频缩略图（写入缓存，生成 Bitmap 后立即删除）
     * @param context   上下文
     * @param videoPath 视频路径
     * @param timeUs    单位微秒
     */
    fun getVideoThumbnail(videoPath: String, timeUs: Long, context: Context): Bitmap? {
        // 临时文件路径
        val tempFile = File(context.cacheDir, "thumb_${System.currentTimeMillis()}.jpg")
        val timeSeconds = timeUs / 1_000_000.0
        // FFmpeg 命令
        val command =
            "-y -ss $timeSeconds -i \"$videoPath\" -vframes 1 -q:v 2 \"${tempFile.absolutePath}\""

        val session = FFmpegKit.execute(command)

        return if (session.returnCode.isValueSuccess) {
            // 读取 Bitmap
            val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
            // 删除临时文件
            tempFile.delete()
            bitmap
        } else {
            tempFile.delete()
            null
        }
    }

    /**
     * 将 WAV 转成 AAC
     * @param wavPath 输入 wav 文件路径
     * @param aacPath 输出 aac 文件路径
     */
    fun wavToAac(wavPath: String, aacPath: String): Boolean {
        val outFile = File(aacPath)
        outFile.parentFile?.mkdirs()

        val cmd = "-y -i \"$wavPath\" -c:a aac \"$aacPath\""
        val session = FFmpegKit.execute(cmd)
        val success = session.returnCode.isValueSuccess

        return success
    }

    /**
     * 获取视频旋转角度
     * @param videoPath 视频文件路径
     * @return 旋转角度，默认 0（未获取到或不旋转）
     */
    fun getVideoRotation(path: String): Int {
        val cmd =
            "-v error -select_streams v:0 -show_entries stream_tags=rotate -of default=noprint_wrappers=1:nokey=1 \"$path\""
        val session = FFprobeKit.execute(cmd)
        val output = session.output?.trim()

        return output?.toIntOrNull() ?: 0 // 没有 rotate 标签时默认返回 0
    }

    fun getVideoWidth(path: String): Int {

        val cmd =
            "-v error -select_streams v:0 -show_entries stream=width -of default=noprint_wrappers=1:nokey=1 \"$path\""
        val session = FFprobeKit.execute(cmd)
        val output = session.output?.trim() ?: return 0
        return output.toIntOrNull() ?: 0;
    }

    fun getVideoHeight(path: String): Int {
        val cmd =
            "-v error -select_streams v:0 -show_entries stream=height -of default=noprint_wrappers=1:nokey=1 \"$path\""
        val session = FFprobeKit.execute(cmd)
        val output = session.output?.trim() ?: return 0
        return output.toIntOrNull() ?: 0
    }
}