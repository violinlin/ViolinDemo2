package com.violin.demo

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import cn.xiaochuankeji.VideoActivity
import com.violin.base.act.BaseBindingAct
import com.violin.base.act.FileUtils
import com.violin.base.act.LogUtil
import com.violin.base.act.exts.binding
import com.violin.base.act.utils.UnzipUtility
import com.violin.demo.databinding.ActivityMainBinding
import com.violin.features.common.CommonActivity
import com.violin.features.common.crash.CrashActivity
import com.violin.features.common.leak.LeakTestActivity
import com.violin.views.views.RecyclerviewActivity
import com.violin.views.views.ViewActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest
import java.util.zip.ZipFile

class MainActivity : BaseBindingAct<ActivityMainBinding>() {
    override val mBinding: ActivityMainBinding by binding<ActivityMainBinding>(R.layout.activity_main)
//    private val mViewModel by viewModels<MainActViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        heartbeat()
    }

    private val pollingTask = Runnable { heartbeat() }
    fun heartbeat() {
        mBinding.btnView.removeCallbacks(pollingTask)
        mBinding.btnView.postDelayed(pollingTask, 12 * 1000)
        LogUtil.d("MainActivity", "heartbeat...")
    }

    override fun setupView() {
        mBinding.btnView.setOnClickListener {
            ViewActivity.start(this)
            LogUtil.d("MainActivity",Log.getStackTraceString(Throwable()))
            mBinding.btnView.post {
                LogUtil.d("MainActivity","----" + Log.getStackTraceString(Throwable()))
                LogUtil.d("MainActivity","----11" + Thread.currentThread().stackTrace)
            }

        }
        mBinding.btnCommon.setOnClickListener {
            CommonActivity.start(this)
            val wevView = WebView(this)
            val webSettings = wevView.settings
//            webSettings.setAppCachePath()
        }
        mBinding.btnVideo.setOnClickListener {
//            VideoActivity.start(this)
            try {
                val assetName = "file_5"
                val zipPath = File(this.getFilesDir(), assetName + ".zip")

                val unzipPath = File(this.getFilesDir(), "unzipfile")
                LogUtil.d("unzip", "path:" + zipPath.absolutePath)
                FileUtils.copyAssetFileToTarget(this, assetName + ".zip", zipPath.absolutePath)
                UnzipUtility.unzip(zipPath, unzipPath)
                val md5 = UnzipUtility.getFileMD5(zipPath)
                val md52 = MD5Utils.getFileMD5(zipPath)
//                unzip(zipPath.absolutePath,unzipPath.absolutePath)
//                unzip(zipPath.absolutePath,unzipPath.absolutePath)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        mBinding.btnCrash.setOnClickListener {
            CrashActivity.start(this)
        }
        mBinding.btnRecyclerview.setOnClickListener {
            RecyclerviewActivity.start(this)
        }
        mBinding.btnLeakTest.setOnClickListener {
            LeakTestActivity.start(this)
        }
        mBinding.btnClipBord.setOnClickListener {
            mBinding.btnClipBord.postDelayed({
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val text = clipboard.primaryClip?.getItemAt(0)?.coerceToText(this)?.toString()
                Toast.makeText(this, text ?: "clip bord is empty", Toast.LENGTH_SHORT).show()
            }, 3000)

        }
    }


    /**
     * 文件 MD5 生成工具类
     */
    object MD5Utils {

        /**
         * 计算文件的 MD5 值
         *
         * @param file 要计算 MD5 值的文件
         * @return 文件的 MD5 值，如果计算失败则返回 null
         */
        fun getFileMD5(file: File): String? {
            if (!file.exists() || !file.isFile) {
                return null
            }

            try {
                val digest = MessageDigest.getInstance("MD5")
                FileInputStream(file).use { fis ->
                    val buffer = ByteArray(1024 * 8)
                    var bytesRead: Int
                    while (fis.read(buffer).also { bytesRead = it } != -1) {
                        digest.update(buffer, 0, bytesRead)
                    }
                }
                return digest.digest().toHex()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * 将字节数组转换为十六进制字符串
         *
         * @return 转换后的十六进制字符串
         */
        private fun ByteArray.toHex(): String {
            val hexString = StringBuilder()
            for (byte in this) {
                val hex = Integer.toHexString(0xFF and byte.toInt())
                if (hex.length == 1) {
                    hexString.append("0") // 补零
                }
                hexString.append(hex)
            }
            return hexString.toString()
        }
    }


    fun unzip(zipFilePath: String, outputDir: String) {
        val file = File(zipFilePath)
        val outputDirectory = File(outputDir)

        // 校验 ZIP 文件是否存在
        if (!file.exists()) {
            throw FileNotFoundException("ZIP 文件不存在: $zipFilePath")
        }

        // 确保输出目录存在
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
            throw IOException("无法创建输出目录: $outputDir")
        }

        // 使用 ZipFile 解压
        ZipFile(file).use { zipFile ->
            val entries = zipFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val outputFile = File(outputDir, entry.name)

                // 检查路径安全性，防止路径穿越攻击
                if (!outputFile.canonicalPath.startsWith(outputDirectory.canonicalPath)) {
                    throw SecurityException("解压路径不安全: ${entry.name}")
                }

                if (entry.isDirectory) {
                    // 如果是目录，创建对应文件夹
                    outputFile.mkdirs()
                } else {
                    // 如果是文件，解压文件
                    outputFile.parentFile?.mkdirs() // 确保父目录存在
                    zipFile.getInputStream(entry).use { inputStream ->
                        FileOutputStream(outputFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
            }
        }
    }
}





