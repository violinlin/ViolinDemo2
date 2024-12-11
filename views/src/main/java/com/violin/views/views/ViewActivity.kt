package com.violin.views.views

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.violin.views.R
import com.violin.views.databinding.ActivityViewBinding
import com.violin.views.views.fallingview.FallingView
import org.libpag.PAGFile
import org.libpag.PAGView

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
//        initFallingView()
        initBannerIV()

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
                    Glide.with(iv_banner.context).load(giftList[currentIndex]).into(iv_banner)
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


    var fallingView: FallingView? = null
    private fun initFallingView() {
        if (fallingView == null) {
            fallingView = FallingView(this)
            addContentView(
                fallingView, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            fallingView?.setDensity(80)
        }

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