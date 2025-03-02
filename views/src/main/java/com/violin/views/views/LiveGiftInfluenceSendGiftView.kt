package com.violin.views.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import com.violin.base.act.UIUtil
import com.violin.views.databinding.LayoutLiveGiftInfluenceSendGiftViewBinding
import org.libpag.PAGFile
import java.util.LinkedList


class LiveGiftInfluenceSendGiftView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {
    var mBinding: LayoutLiveGiftInfluenceSendGiftViewBinding
    val addNumList = LinkedList<Int>()

    init {
        mBinding = LayoutLiveGiftInfluenceSendGiftViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )


    }


    private var cNumber = 0
    private var isDetachedFromWindow = false
    var cNumAnimator: ValueAnimator? = null
    var isAnimating = false
    val mTransFormY = UIUtil.dp2px(10f, context)
    fun addTicket(ticket: Int) {
        if (isAnimating) {
            addNumList.add(ticket)
        } else {
            playAnima(ticket)
        }
    }

    private fun playAnima(addNum: Int) {
        val preNum = cNumber
        cNumber += addNum
        cNumAnimator = ValueAnimator.ofInt(preNum, cNumber)

        cNumAnimator?.duration = if (addNum <= 10) {
            300
        } else {
            800
        }
        cNumAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            mBinding.tvInfluenceBannerTicketCount.text = value.toString() // 更新 TextView 显示的数字
        }
        cNumAnimator?.start()
        playAddTvAnim(addNum)
    }

    var mAddNumTVFinalSet: AnimatorSet? = null
    private fun playAddTvAnim(addNum: Int) {
        isAnimating = true// 以时间最长的动画为结束点
        setPagRes()
        mBinding.pagInfluenceAddTicket.progress = 0.0
        mBinding.pagInfluenceAddTicket.play()
        mBinding.tvInfluenceAddTicket.text = "+$addNum"

        val textView = mBinding.tvInfluenceAddTicket
        // 第一阶段：0-160ms 缩放 0→1.5，透明度 0→1
        val scaleXAnim1 = ObjectAnimator.ofFloat(textView, "scaleX", 0f, 1.5f)
        val scaleYAnim1 = ObjectAnimator.ofFloat(textView, "scaleY", 0f, 1.5f)
        val alphaAnim1 = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)

        val set1 = AnimatorSet()
        set1.playTogether(scaleXAnim1, scaleYAnim1, alphaAnim1)
        set1.setDuration(160)


        // 第二阶段：160-320ms 缩放 1.5→1.0
        val scaleXAnim2 = ObjectAnimator.ofFloat(textView, "scaleX", 1.5f, 1f)
        val scaleYAnim2 = ObjectAnimator.ofFloat(textView, "scaleY", 1.5f, 1f)

        val set2 = AnimatorSet()
        set2.playTogether(scaleXAnim2, scaleYAnim2)
        set2.startDelay = 160 // 从 160ms 开始
        set2.setDuration(160) // 持续到 320ms


        // 第三阶段：400ms 开始 透明度 1→0，上移 32dp
        val alphaAnim3 = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f)
        val transYAnim = ObjectAnimator.ofFloat(
            textView,
            "translationY",
            0f,
            -mTransFormY,
        )


        val set3 = AnimatorSet()
        set3.playTogether(alphaAnim3, transYAnim)
        set3.startDelay = 500 // 从 500ms 开始
        set3.setDuration(400) // 默认持续 400ms

        // 组合所有动画
        mAddNumTVFinalSet = AnimatorSet()
        mAddNumTVFinalSet?.playSequentially(set1, set2, set3)
        mAddNumTVFinalSet?.start()

        mAddNumTVFinalSet?.doOnEnd {
            if (!isDetachedFromWindow) {
                textView.translationY = 0f
                isAnimating = false
                val num = addNumList.pollFirst()
                num?.let {
                    addTicket(it)
                }
            }

        }


    }


    private fun setPagRes() {
        if (mBinding.pagInfluenceAddTicket.composition == null) {
            val composition = PAGFile.Load(context.assets, "anim_ticket_add.pag")
            mBinding.pagInfluenceAddTicket.setRepeatCount(1)
            mBinding.pagInfluenceAddTicket.composition = composition
        }
    }

    override fun onDetachedFromWindow() {
        isDetachedFromWindow = true
        addNumList.clear()
        cNumAnimator?.cancel()
        cNumAnimator = null
        mAddNumTVFinalSet?.cancel()
        mAddNumTVFinalSet = null
        super.onDetachedFromWindow()
    }

}