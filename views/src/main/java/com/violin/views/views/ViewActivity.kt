package com.violin.views.views

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.drake.spannable.addSpan
import com.drake.spannable.setSpan
import com.violin.base.act.UIUtil
import com.violin.views.R
import com.violin.views.databinding.ActivityViewBinding
import com.violin.views.views.fallingview.FallingSurfaceView
import com.violin.views.views.fallingview.snowfalll.SnowFallInterface
import com.violin.views.views.fallingview.snowfalll.SnowParamsConfig
import com.violin.views.views.fallingview.snowfalll.SnowfallView
import org.libpag.PAGFile


class ViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    var count = 0
    private lateinit var binding: ActivityViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPlaceHolderDrawable()
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
        binding.btnViewPager.setOnClickListener {
            ViewPagerActivity.start(this)
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
        var fallingView: FallingSurfaceView? = null
        var view: SnowFallInterface? = null
        binding.btnGiftFalling.setOnClickListener {
            binding.flGiftFallingContainerMatch.removeAllViews()
//            if (fallingView != null) {
//                fallingView?.addFakes(10)
//                return@setOnClickListener
//            }
//            FallingSurfaceView.startAnim(
//                FallingViewConfig(
//                    icon = "https://img01.mehiya.com/img/png/id/244767108462",
//                    animTimeSecond = -1,
//                    iconSizePX = UIUtil.dp2px(36F, this@ViewActivity).toInt(),
//                    maxDensity = 1,
//                    direction = 1,
//                    isMoveX = false
//                ),
//                this,
//                binding.flGiftFallingContainer
//            ) { resultView ->
//                fallingView = resultView
//            }

            if (view != null) {
                view?.addFakes(3)
                return@setOnClickListener
            }
            SnowfallView.startAnim(
                SnowParamsConfig(
                    imageUrl = "http://me-media-gateway.test03.youyisia.com/accountv2/view/id/1262556591/sz/src",
                    direction = 1,
                    speedMin = 4,
                    speedMax = 6,
                    sizeMaxInPx = UIUtil.dp2px(36F, this).toInt(),
                    sizeMinInPx = UIUtil.dp2px(36F * 1.5F, this).toInt(),
                    snowflakesNum = 4,
                    isMoveX = false,
                    fadingEnabled = true
                ), binding.flGiftFallingContainer, startCallback = {
                    view = it
                })


        }
        binding.btnGiftFallingDown.setOnClickListener {
            dumpViewHierarchy(binding.main)
            binding.flGiftFallingContainer.removeAllViews()
            var count = 20
            var size = 80
            var minSpeed = 10
            var alpha = true
            var isMoveX = true
            val nums = binding.etFalling.text.split(":")
            try {
                count = nums[0].toInt()
                size = nums[1].toInt()
//                minSpeed = nums[2].toInt()
                alpha = nums[3].toInt() == 1
                isMoveX = nums[4].toInt() == 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
////            FallingSurfaceView.startAnim(
////                FallingViewConfig(
////                    icon = "https://img01.mehiya.com/img/png/id/244767108462",
////                    iconSizePX = UIUtil.dp2px(iconSizePX.toFloat(), this@ViewActivity).toInt(),
////                    maxDensity = maxDensity,
////                    ySpeedBuffer = ySpeedBuffer,
////                    delayTime = delayTime,
////                    isMoveX = isMoveX,
////                    sizeScale = scale
////                ),
////                this,
////                binding.flGiftFallingContainerMatch
////            )
//
////            FallingView.startAnim(
////                FallingViewConfig(
////                    icon = "https://img01.mehiya.com/img/png/id/244767108462",
////                    iconSizePX = UIUtil.dp2px(iconSizePX.toFloat(), this@ViewActivity).toInt(),
////                    maxDensity = maxDensity,
////                    ySpeedBuffer = ySpeedBuffer,
////                    delayTime = delayTime,
////                    isMoveX = isMoveX,
////                    sizeScale = scale
////                ),
////                this,
////                binding.flGiftFallingContainer1
////            ){
////
////            }
//
////            SnowfallView.startAnim(
////                SnowParamsConfig(
////                    imageUrl = "https://img01.mehiya.com/img/png/id/244767108462",
////                    speedMin = minSpeed,
////                    speedMax = (minSpeed * 1.5).toInt(),
////                    sizeMaxInPx = UIUtil.dp2px((120F), this).toInt(),
////                    sizeMinInPx = UIUtil.dp2px(110F, this).toInt(),
////                    snowflakesNum = count,
////                    fadingEnabled = alpha,
////                    isMoveX = isMoveX
//////                    animTimeSecond = 3
////                ), binding.flGiftFallingContainer1
////            )
//
            SnowfallView.startAnim(
                SnowParamsConfig(
                    imageUrl = "https://img01.mehiya.com/img/png/id/244767108462",
                    speedMin = minSpeed,
                    speedMax = (minSpeed * 1.5).toInt(),
                    sizeMaxInPx = UIUtil.dp2px((80F), this).toInt(),
                    sizeMinInPx = UIUtil.dp2px(60F, this).toInt(),
                    snowflakesNum = count,
                    isMoveX = isMoveX,
                    animTimeSecond = 3
                ), binding.flGiftFallingContainer1
            )
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

        binding.tvTextUnderline.paintFlags =
            binding.tvTextUnderline.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun initPlaceHolderDrawable() {
        val placeDrawable = createLayerDrawable(
            this,
            gradientColors = intArrayOf(
                Color.parseColor("#32323F"),
                Color.parseColor("#32323F")
            ),
            cornerRadius = UIUtil.dp2px(8F, this)
        )
        placeDrawable?.let {
            binding.flDrawablePlaceholder.background = it
        }
    }

    fun createLayerDrawable(
        context: Context,
        gradientColors: IntArray,
        cornerRadius: Float
    ): Drawable? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val logoDrawable = ContextCompat.getDrawable(context, R.drawable.logo2)
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                gradientColors
            ).apply {
                this.cornerRadius = cornerRadius
            }
            val layers = arrayOf<Drawable>(gradientDrawable, logoDrawable!!)
            val layerDrawable = LayerDrawable(layers)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layerDrawable.setLayerGravity(1, Gravity.CENTER)
            }
            return layerDrawable
        } else {
            return null
        }
    }

    private fun dumpViewHierarchy(view: View?) {
        if (view == null) {
            return
        }
        dumpViewRecursive(view, 0)
    }

    private fun dumpViewRecursive(view: View, depth: Int) {
        try {
            if (view is ViewGroup) {
                val group = view
                for (i in 0 until group.childCount) {
                    var child = group.getChildAt(i)
                    if (child == null) { // 重点：记录空子视图位置
                        val parentViewInfo =
                            "parent:${group.javaClass.simpleName}," +
                                    "id:${getViewIDName(group)}," +
                                    "childIndex:${i},depth:${depth}"
                        Log.d("ViewDump", parentViewInfo)
                        break
                    } else {
                        dumpViewRecursive(child, depth + 1)
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getViewIDName(view: View): String {
        // 获取视图ID信息
        var idName = "null"
        if (view.id != View.NO_ID) {
            try {
                idName = view.resources.getResourceName(view.id)
            } catch (ignored: java.lang.Exception) {
            }
        }
        return idName
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