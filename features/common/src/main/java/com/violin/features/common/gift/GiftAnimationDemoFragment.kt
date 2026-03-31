package com.violin.features.common.gift

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.violin.base.act.BaseFragment
import com.violin.fretures.common.databinding.FragmentGiftAnimationDemoBinding
import kotlin.random.Random

class GiftAnimationDemoFragment : BaseFragment<FragmentGiftAnimationDemoBinding>() {

    private var stage: FrameLayout? = null
    private var driftMode: DriftMode = DriftMode.CONVERGE
    private val pendingStageTasks = mutableListOf<Runnable>()
    private val runningAnimators = mutableSetOf<Animator>()

    private enum class DriftMode {
        CONVERGE,
        SPREAD
    }

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
            playComboAnimation()
        }
        binding.btnClearStage.setOnClickListener {
            clearGiftViews()
        }
        val roseTrailBtnId =
            resources.getIdentifier("btn_rose_trail", "id", requireContext().packageName)
        binding.root.findViewById<View?>(roseTrailBtnId)?.setOnClickListener {
            playRoseTrailAnimation()
        }
        val switchId =
            resources.getIdentifier("switch_drift_mode", "id", requireContext().packageName)
        val driftModeSwitch = binding.root.findViewById<SwitchCompat?>(switchId)
        driftModeSwitch?.setOnCheckedChangeListener { _, isChecked ->
            driftMode = if (isChecked) DriftMode.SPREAD else DriftMode.CONVERGE
            driftModeSwitch.text = if (isChecked) {
                "当前模式：发散型（长距离飘动）"
            } else {
                "当前模式：收束型（短距离飘动）"
            }
        }
    }

    private fun playGiftAnimation() {
        val stage = stage ?: return
        if (stage.width == 0 || stage.height == 0) {
            postStageTask { playGiftAnimation() }
            return
        }

        val icon = ImageView(requireContext()).apply {
            setImageResource(android.R.drawable.btn_star_big_on)
            alpha = 0f
            scaleX = 0.7f
            scaleY = 0.7f
        }
        val size = dp(36)
        val startX = ((stage.width - size) / 2f)
        val startY = (stage.height - size).toFloat()
        val centerStopY = ((stage.height - size) / 2f)
        val riseToCenter = startY - centerStopY
        val params = FrameLayout.LayoutParams(size, size).apply {
            gravity = Gravity.TOP or Gravity.START
            leftMargin = startX.toInt()
            topMargin = startY.toInt()
        }
        stage.addView(icon, params)

        val moveToCenter = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(icon, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(icon, View.TRANSLATION_Y, 0f, -riseToCenter),
                ObjectAnimator.ofFloat(icon, View.SCALE_X, 0.7f, 1.08f, 1f),
                ObjectAnimator.ofFloat(icon, View.SCALE_Y, 0.7f, 1.08f, 1f)
            )
            duration = 420
        }

        val holdAtCenter = ObjectAnimator.ofFloat(icon, View.ALPHA, 1f, 1f).apply { duration = 220 }
        val driftAway = createDriftAnimator(icon, riseToCenter)

        trackAnimator(AnimatorSet().apply {
            playSequentially(moveToCenter, holdAtCenter, driftAway)
            doOnEnd { stage.removeView(icon) }
            start()
        })
    }

    private fun playComboAnimation() {
        val stage = stage ?: return
        if (stage.width == 0 || stage.height == 0) {
            postStageTask { playComboAnimation() }
            return
        }

        val total = 8
        val size = dp(36)
        val startX = ((stage.width - size) / 2f)
        val startY = (stage.height - size).toFloat()
        val centerStopY = ((stage.height - size) / 2f)
        val riseToCenter = startY - centerStopY
        val icons = mutableListOf<ImageView>()
        var arrivedCount = 0

        repeat(total) { index ->
            postStageTask(index * 90L) {
                val icon = ImageView(requireContext()).apply {
                    setImageResource(android.R.drawable.btn_star_big_on)
                    alpha = 0f
                    scaleX = 0.7f
                    scaleY = 0.7f
                }
                val params = FrameLayout.LayoutParams(size, size).apply {
                    gravity = Gravity.TOP or Gravity.START
                    leftMargin = startX.toInt()
                    topMargin = startY.toInt()
                }
                stage.addView(icon, params)
                icons.add(icon)

                trackAnimator(AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(icon, View.ALPHA, 0f, 1f),
                        ObjectAnimator.ofFloat(icon, View.TRANSLATION_Y, 0f, -riseToCenter),
                        ObjectAnimator.ofFloat(icon, View.SCALE_X, 0.7f, 1.06f, 1f),
                        ObjectAnimator.ofFloat(icon, View.SCALE_Y, 0.7f, 1.06f, 1f)
                    )
                    duration = 360
                    doOnEnd {
                        arrivedCount++
                        if (arrivedCount == total) {
                            postStageTask(180L) {
                                icons.forEach { centerIcon ->
                                    trackAnimator(
                                        createDriftAnimator(
                                            centerIcon,
                                            riseToCenter
                                        ).apply {
                                            doOnEnd { stage.removeView(centerIcon) }
                                            start()
                                        })
                                }
                            }
                        }
                    }
                    start()
                })
            }
        }
    }

    private fun createDriftAnimator(icon: ImageView, riseToCenter: Float): AnimatorSet {
        val driftDirection = if (Random.nextBoolean()) 1 else -1
        val (driftXMin, driftXMax, driftUpMin, driftUpMax) = when (driftMode) {
            DriftMode.CONVERGE -> arrayOf(dp(36), dp(86), dp(84), dp(146))
            DriftMode.SPREAD -> arrayOf(dp(120), dp(220), dp(160), dp(280))
        }
        val driftX = driftDirection * Random.nextInt(driftXMin, driftXMax).toFloat()
        val driftUp = Random.nextInt(driftUpMin, driftUpMax).toFloat()
        return AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(icon, View.TRANSLATION_X, 0f, driftX),
                ObjectAnimator.ofFloat(
                    icon,
                    View.TRANSLATION_Y,
                    -riseToCenter,
                    -riseToCenter - driftUp
                ),
                ObjectAnimator.ofFloat(icon, View.ALPHA, 1f, 0f),
                ObjectAnimator.ofFloat(icon, View.SCALE_X, 1f, 0.92f),
                ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1f, 0.92f)
            )
            duration = 560
        }
    }

    private fun playRoseTrailAnimation() {
        val stage = stage ?: return
        if (stage.width == 0 || stage.height == 0) {
            postStageTask { playRoseTrailAnimation() }
            return
        }
        val size = dp(34)
        val startX = dp(14).toFloat()
        val startY = -size.toFloat()
        val endX = ((stage.width - size) / 2f)
        val endY = ((stage.height - size) / 2f)
        val roseCount = 8
        val spawnInterval = 90L
        val travelDuration = 1020L
        val roses = mutableListOf<ImageView>()
        var arrivedCount = 0
        fun playGiftTrailBackAnimationInner() {
            val topRightX = (stage.width - size - dp(14)).toFloat() - startX
            val topRightY = dp(12).toFloat() - startY
            var movedCount = 0
            roses.forEachIndexed { trailIndex, targetRose ->
                postStageTask(trailIndex * 65L) {
                    val endAlpha = (0.8f - trailIndex * 0.08f).coerceAtLeast(0.18f)
                    val endScale = (0.96f - trailIndex * 0.05f).coerceAtLeast(0.62f)
                    trackAnimator(AnimatorSet().apply {
                        playTogether(
                            ObjectAnimator.ofFloat(
                                targetRose,
                                View.TRANSLATION_X,
                                targetRose.translationX,
                                topRightX
                            ),
                            ObjectAnimator.ofFloat(
                                targetRose,
                                View.TRANSLATION_Y,
                                targetRose.translationY,
                                topRightY
                            ),
                            ObjectAnimator.ofFloat(
                                targetRose,
                                View.ALPHA,
                                targetRose.alpha,
                                endAlpha
                            ),
                            ObjectAnimator.ofFloat(
                                targetRose,
                                View.SCALE_X,
                                targetRose.scaleX,
                                endScale
                            ),
                            ObjectAnimator.ofFloat(
                                targetRose,
                                View.SCALE_Y,
                                targetRose.scaleY,
                                endScale
                            )
                        )
                        duration = 420
                        doOnEnd {
                            stage.removeView(targetRose)
                            movedCount++
                            if (movedCount == roseCount) {
                                roses.forEach { finalRose ->
                                    trackAnimator(
                                        ObjectAnimator.ofFloat(
                                            finalRose,
                                            View.ALPHA,
                                            finalRose.alpha,
                                            0f
                                        ).apply {
                                            duration = 220
                                            doOnEnd { stage.removeView(finalRose) }
                                            start()
                                        })
                                }
                            }
                        }
                        start()
                    })
                }
            }
        }

        fun playGiftTrailSendAnimationInner(imageView: ImageView) {
            trackAnimator(AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0f, endX - startX),
                    ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, 0f, endY - startY),
                    ObjectAnimator.ofFloat(imageView, View.ALPHA, imageView.alpha, 1f),
                    ObjectAnimator.ofFloat(imageView, View.SCALE_X, imageView.scaleX, 1.05f),
                    ObjectAnimator.ofFloat(imageView, View.SCALE_Y, imageView.scaleY, 1.05f)
                )
                duration = travelDuration
                doOnEnd {
                    arrivedCount++
                    if (arrivedCount == roseCount) {
                        playGiftTrailBackAnimationInner()
                    }
                }
                start()
            })
        }
        repeat(roseCount) { index ->
            val rose = ImageView(requireContext()).apply {
                // 可替换成你的玫瑰资源：setImageResource(R.drawable.your_rose)
                setImageResource(android.R.drawable.btn_star_big_on)
                alpha = 0.05f + index * 0.025f
                scaleX = 0.9f
                scaleY = 0.9f
            }
            val params = FrameLayout.LayoutParams(size, size).apply {
                gravity = Gravity.TOP or Gravity.START
                leftMargin = startX.toInt()
                topMargin = startY.toInt()
            }
            stage.addView(rose, params)
            roses.add(rose)
        }
        for ((index, iv) in roses.asReversed().withIndex()) {
            postStageTask(index * spawnInterval) {
                playGiftTrailSendAnimationInner(iv)
            }
        }


    }

    private fun clearGiftViews() {
        val stage = stage ?: return
        stage.children.toList()
            .filterIsInstance<ImageView>()
            .forEach { stage.removeView(it) }
    }

    override fun onDestroyView() {
        cancelAllRunningAnimation()
        super.onDestroyView()
        stage = null
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    private fun postStageTask(delayMs: Long = 0L, block: () -> Unit) {
        val stage = stage ?: return
        val task = Runnable { block() }
        pendingStageTasks.add(task)
        if (delayMs <= 0L) {
            stage.post(task)
        } else {
            stage.postDelayed(task, delayMs)
        }
    }

    private fun trackAnimator(animator: Animator): Animator {
        runningAnimators.add(animator)
        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                runningAnimators.remove(animation)
            }

            override fun onAnimationCancel(animation: Animator) {
                runningAnimators.remove(animation)
            }
        })
        return animator
    }

    private fun cancelAllRunningAnimation() {
        val stage = stage
        pendingStageTasks.forEach { task ->
            stage?.removeCallbacks(task)
        }
        pendingStageTasks.clear()

        runningAnimators.toList().forEach { animator ->
            animator.cancel()
        }
        runningAnimators.clear()
    }
}
