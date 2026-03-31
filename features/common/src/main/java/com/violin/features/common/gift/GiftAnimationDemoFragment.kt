package com.violin.features.common.gift

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.violin.base.act.BaseFragment
import com.violin.fretures.common.databinding.FragmentGiftAnimationDemoBinding
import kotlin.math.max
import kotlin.random.Random

class GiftAnimationDemoFragment : BaseFragment<FragmentGiftAnimationDemoBinding>() {

    private var stage: FrameLayout? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGiftAnimationDemoBinding {
        return FragmentGiftAnimationDemoBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        stage = binding.flStage
        binding.btnSingleGift.setOnClickListener {
            playGiftAnimation()
        }
        binding.btnComboGift.setOnClickListener {
            repeat(8) { index ->
                stage?.postDelayed({ playGiftAnimation() }, index * 120L)
            }
        }
        binding.btnClearStage.setOnClickListener {
            clearGiftViews()
        }
    }

    private fun playGiftAnimation() {
        val stage = stage ?: return
        if (stage.width == 0 || stage.height == 0) {
            stage.post { playGiftAnimation() }
            return
        }

        val icon = ImageView(requireContext()).apply {
            setImageResource(android.R.drawable.btn_star_big_on)
            alpha = 0f
            scaleX = 0.7f
            scaleY = 0.7f
        }
        val size = dp(36)
        val maxLeft = max(1, stage.width - size)
        val startX = Random.nextInt(0, maxLeft).toFloat()
        val startY = (stage.height - size).toFloat()
        val params = FrameLayout.LayoutParams(size, size).apply {
            gravity = Gravity.TOP or Gravity.START
            leftMargin = startX.toInt()
            topMargin = startY.toInt()
        }
        stage.addView(icon, params)

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(icon, View.ALPHA, 0f, 1f, 1f, 0f),
                ObjectAnimator.ofFloat(icon, View.TRANSLATION_Y, 0f, -dp(180).toFloat()),
                ObjectAnimator.ofFloat(icon, View.SCALE_X, 0.7f, 1.15f, 1f),
                ObjectAnimator.ofFloat(icon, View.SCALE_Y, 0.7f, 1.15f, 1f)
            )
            duration = 900
            doOnEnd { stage.removeView(icon) }
            start()
        }
    }

    private fun clearGiftViews() {
        val stage = stage ?: return
        stage.children.toList()
            .filterIsInstance<ImageView>()
            .forEach { stage.removeView(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stage = null
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}
