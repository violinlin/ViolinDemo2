package com.violin.views.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.violin.views.databinding.ViewInfluenceViewBinding
import com.violin.views.views.adapter.RecyclerViewAdapter
import org.libpag.PAGFile

class InfluenceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var mBinding: ViewInfluenceViewBinding

    var isClear: Boolean = false

    init {
        mBinding = ViewInfluenceViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        setOnClickListener {
            if (!isClear) {
                Glide.with(mBinding.ivCenter).clear(mBinding.ivCenter)
//                mBinding.pagLeft.composition = null
                mBinding.pagLeft.apply {
                    stop()
                    composition = null
                    postDelayed({invalidate()},1000)
                    onDetachedFromWindow()
                }
                mBinding.pagRight.apply {
                    stop()
                    composition = null
                    postDelayed({invalidate()},1000)
                }
                isClear = true
            } else {
                Glide.with(mBinding.ivCenter)
                    .load("$host/influence/icon_center.png")
                    .into(mBinding.ivCenter)

                mBinding.pagLeft.apply {
                    path = "$host/gold_miner_lord_0.pag"
                    setRepeatCount(0)
                    play()
                }

                mBinding.pagRight.apply {
                    path = "$host/gold_miner_lord_1.pag"
                    setRepeatCount(0)
                    play()
                }
                isClear = false
            }

        }
    }

    val host = "http://192.168.25.25:8000"

    fun setData(data: RecyclerViewAdapter.MyBean) {
        Glide.with(mBinding.ivLeft)
            .load("$host/influence/icon_left.png")
            .into(mBinding.ivLeft)


        Glide.with(mBinding.ivCenter)
            .load("$host/influence/icon_center.png")
            .into(mBinding.ivCenter)

        Glide.with(mBinding.ivRight)
            .load("$host/influence/icon_right.png")
            .into(mBinding.ivRight)


//        Glide.with(mBinding.ivCoverLeft)
//            .load("$host/influence/icon_cover_left.png")
//            .into(mBinding.ivCoverLeft)
//
//        Glide.with(mBinding.ivCoverRight)
//            .load("$host/influence/icon_cover_right.png")
//            .into(mBinding.ivCoverRight)


//        PAGFile.LoadAsync("$host/gold_miner_lord_0.pag",
//            object : PAGFile.LoadListener {
//                override fun onLoad(pagFIle: PAGFile?) {
//                    mBinding.pagLeft.apply {
//                        composition = pagFIle
//                        setRepeatCount(-1)
//                        play()
//                    }
//
//                }
//
//            })
//        mBinding.pagLeft.apply {
//            path = "$host/gold_miner_lord_0.pag"
//            setRepeatCount(0)
//            play()
////            if (visibility == View.VISIBLE) {
////                visibility = View.GONE
////            } else {
////                visibility = View.VISIBLE
////            }
//        }

//        PAGFile.LoadAsync("$host/gold_miner_lord_1.pag",
//            object : PAGFile.LoadListener {
//                override fun onLoad(pagFIle: PAGFile?) {
//                    mBinding.pagRight.apply {
//                        composition = pagFIle
//                        setRepeatCount(-1)
//                        play()
//                        post({
////                            if (visibility == View.VISIBLE) {
////                                visibility = View.GONE
////                            } else {
////                                visibility = View.VISIBLE
////                            }
//                        })
//
//                    }
//
//                }
//
//            })
//        val mcomposition = PAGFile.Load(context.assets, "anim_lucky_gift_level_3.pag")
//        mBinding.pagLeft.apply {
//            composition = mcomposition
//            setRepeatCount(0)
//            play()
//        }
//
//        val mcompositionRight = PAGFile.Load(context.assets, "anim_lucky_gift_level_4.pag")
//        mBinding.pagRight.apply {
//            composition = mcompositionRight
//            setRepeatCount(0)
//            play()
//        }

    }

}