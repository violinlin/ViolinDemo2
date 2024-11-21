package com.violin.demo

import android.os.Bundle
import cn.xiaochuankeji.VideoActivity
import com.violin.base.act.BaseBindingAct
import com.violin.base.act.FileUtils
import com.violin.base.act.LogUtil
import com.violin.base.act.exts.binding
import com.violin.base.act.utils.UnzipUtility
import com.violin.demo.databinding.ActivityMainBinding
import com.violin.features.common.CommonActivity
import com.violin.views.views.ViewActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

class MainActivity : BaseBindingAct<ActivityMainBinding>() {
    override val mBinding: ActivityMainBinding by binding<ActivityMainBinding>(R.layout.activity_main)
//    private val mViewModel by viewModels<MainActViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupView() {
        mBinding.btnView.setOnClickListener {
            ViewActivity.start(this)

        }
        mBinding.btnCommon.setOnClickListener {
            CommonActivity.start(this)
        }
        mBinding.btnVideo.setOnClickListener {
//            VideoActivity.start(this)
            try {
                val assetName = "file_test_4"
                val zipPath = File(this.getFilesDir(), assetName + ".zip")
                val unzipPath = File(this.getFilesDir(), "unzipfile")
                LogUtil.d("unzip", "path:" + zipPath.absolutePath)
                FileUtils.copyAssetFileToTarget(this, assetName + ".zip", zipPath.absolutePath)
                UnzipUtility.unzip(zipPath, unzipPath)
//                unzip(zipPath.absolutePath,unzipPath.absolutePath)
//                unzip(zipPath.absolutePath,unzipPath.absolutePath)

            } catch (e: Exception) {
                e.printStackTrace()
            }

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
    }}





