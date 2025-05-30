package com.violin.views.views

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.InputType
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.drake.spannable.addSpan
import com.drake.spannable.setSpan
import com.violin.base.act.UIUtil
import com.violin.views.R
import com.violin.views.databinding.ActivityViewBinding
import com.violin.views.views.fallingview.FallingSurfaceView
import com.violin.views.views.fallingview.FallingView
import org.libpag.PAGFile

class ViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    var count = 0
    private lateinit var binding: ActivityViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pagview.apply {
            composition = PAGFile.Load(assets, "piaopingdiban.pag")
            setRepeatCount(-1)
            play()
            val animlist = arrayOf(
                "anim_lucky_gift_level_3.pag",
                "anim_lucky_gift_level_4.pag",
                "bg03_bmp.pag",
                "bg04_bmp.pag",
                "half_bmp.pag"
            )
            setOnClickListener {
                count++
                composition = PAGFile.Load(assets, animlist[count % animlist.size])
                Log.d("pagView:", "duration" + composition.duration())
                setRepeatCount(-1)
                play()

            }
        }

        initViewFlipper()
        initBannerIV()
        val array = intArrayOf(10000)// 632
        binding.btnAddTicket.setOnClickListener {
            for (item in array) {
                binding.viewSendGift.addTicket(item)
            }
            if (binding.btnAddTicket.background == null) {
                GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = UIUtil.dp2px(30F, baseContext)
                    setColor(Color.RED)
                    binding.btnAddTicket.setBackgroundDrawable(this)
                }
            } else {
                binding.btnAddTicket.setBackgroundDrawable(null)
            }

        }
        binding.btnGiftFalling.setOnClickListener {
            FallingSurfaceView.startAnim(
                FallingSurfaceView.GiftFallingJson(icon = "https://img01.mehiya.com/img/png/id/244767108462"),
                this,
                binding.flGiftFallingContainer
            )

//            FallingView.startAnim(
//                FallingView.GiftFallingJson(icon = "https://img01.mehiya.com/img/png/id/244767108462"),
//                this,
//                binding.flGiftFallingContainer1
//            )
        }

        binding.textview.text =
            "16".setSpan(AbsoluteSizeSpan(18, true))
                .addSpan("&", AbsoluteSizeSpan(9, true))
                .addSpan("OFF", AbsoluteSizeSpan(18, true))

        val pagFile = PAGFile.Load(assets, "replace_text.pag")
        val text1 = pagFile.getTextData(0)
        text1.text = "测试 0"
        val text2 = pagFile.getTextData(1)
        text2.text = "测试 1"
        binding.pagReplaceText.composition = pagFile
        pagFile.replaceText(0, text1)
        pagFile.replaceText(1, text2)

//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.share_bg)
//        val drawable = TopCropDrawable(bitmap)
//        binding.flTopCropBg.background = drawable
    }

    private val images = listOf(
        "https://img01.mehiya.com/img/png/id/232939154967",
        "https://img01.mehiya.com/img/png/id/232939137094"
    )
    private var currentIndex = 0
    var iv_banner: ImageView? = null
    private fun initBannerIV() {
        iv_banner = findViewById<ImageView>(R.id.iv_banner)
        startImageSlideshow()


        val et = findViewById<EditText>(R.id.et)
        et.inputType = InputType.TYPE_CLASS_NUMBER
        et.setText("hello")
//        et.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        iv_banner?.setOnClickListener {
            val string = et.text
        }
    }

    var showAnim: ObjectAnimator? = null
    val showNextRunnable = Runnable {
        images.let { giftList ->
            iv_banner?.let { iv_banner ->
                showAnim = ObjectAnimator.ofFloat(iv_banner, "alpha", 1f, 0f)
                    .setDuration(320)
                showAnim?.doOnEnd {
                    currentIndex = (currentIndex + 1) % giftList.size
                    Log.d("ViewActivity", "showAnim...." + this.isDestroyed)
                    if (!this.isDestroyed) {
                        Glide.with(iv_banner).load(giftList[currentIndex]).into(iv_banner)
                    }
                    showAnim = ObjectAnimator.ofFloat(iv_banner, "alpha", 0f, 1f)
                        .setDuration(320)
                    showAnim?.doOnEnd {

                        showNextImage()
                    }
                    showAnim?.start()

                }
                showAnim?.start()
            }

        }
    }

    fun startImageSlideshow() {
        currentIndex = 0
        iv_banner?.let {
            Glide.with(it).load(images[currentIndex]).into(it)
        }
        showNextImage()
    }

    fun showNextImage() {
        window.decorView.postDelayed(showNextRunnable, 360)

    }

    override fun onDestroy() {
        Log.d("ViewActivity", "onDestroy")
        showAnim?.cancel()
        window.decorView.removeCallbacks(showNextRunnable)
        super.onDestroy()
    }


    private fun initViewFlipper() {
        val tv2 = findViewById<TextView>(R.id.tv2)
//        val view1 = TextView(this)
//        view1.text = "111111111"
//        view1.setBackgroundColor(Color.RED)
//        view_flipper.addView(view1)
//
//        val view2 = TextView(this)
//        view2.text = "2222222222"
//        view2.setBackgroundColor(Color.BLUE)
//        view_flipper.addView(view2)

        binding.viewFlipper.apply {
            flipInterval = 3 * 1000
            startFlipping()
            setOnClickListener {
                if (tv2.visibility != View.VISIBLE) {
                    tv2.visibility = View.VISIBLE
                    startFlipping()
                } else {
                    tv2.visibility = View.GONE

                    val dis = displayedChild
                    if (dis != 0) {
                        showNext()
                    }

                    stopFlipping()
                }

            }
        }


    }


    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ViewActivity::class.java)
            context.startActivity(starter)
        }
    }
}