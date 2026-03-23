package com.violin.features.common.image

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.violin.base.act.BaseFragment
import com.violin.fretures.common.databinding.FragmentImagePickerDemoBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class ImagePickerDemoFragment : BaseFragment<FragmentImagePickerDemoBinding>() {

    companion object {
        /** 裁切固定宽高比，可改为 4f/3f、16f/9f 等 */
        private const val CROP_RATIO_X = 1f
        private const val CROP_RATIO_Y = 1f
    }

    private val cropImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val data = result.data ?: return@registerForActivityResult
            val outUri = UCrop.getOutput(data) ?: return@registerForActivityResult
            Glide.with(this).load(outUri).into(binding.ivPreview)
        }

    private val pickVisualMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                startCrop(uri)
            }
        }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentImagePickerDemoBinding {
        return FragmentImagePickerDemoBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.btnPickImage.setOnClickListener {
            pickVisualMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun startCrop(sourceUri: Uri) {
        val ctx = requireContext()
        val dir = File(ctx.cacheDir, "my_internal_cache").apply { mkdirs() }
        val destFile = File(dir, "cropped_${System.currentTimeMillis()}.jpg")
        val destUri = FileProvider.getUriForFile(
            ctx,
            "${ctx.packageName}.fileprovider",
            destFile
        )
        val options = UCrop.Options().apply {
            setCompressionQuality(90)
            setFreeStyleCropEnabled(false)
        }
        val intent = UCrop.of(sourceUri, destUri)
            .withAspectRatio(CROP_RATIO_X, CROP_RATIO_Y)
            .withOptions(options)
            .getIntent(ctx)
        cropImage.launch(intent)
    }
}
