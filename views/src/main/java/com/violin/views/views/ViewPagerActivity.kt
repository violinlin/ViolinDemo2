package com.violin.views.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.CompositePageTransformer
import com.violin.base.act.UIUtil
import com.violin.views.R
import com.violin.views.databinding.ActivityViewPagerBinding
import com.violin.views.views.viewpager.ViewPagerAdapter

class ViewPagerActivity : AppCompatActivity() {
    private val TAG = "ViewPagerActivity"
    private lateinit var binding: ActivityViewPagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        enableEdgeToEdge()
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initVP()

    }

    private fun setPage(pageIndex: Int) {
        if (binding.viewpager.currentItem == pageIndex) {
            return
        }
        binding.viewpager.currentItem = pageIndex
    }

    private fun initVP() {
        binding.viewNextPage.setOnClickListener {
            val nextPage = binding.viewpager.currentItem + 1
            if (nextPage < items.size) {
                setPage(nextPage)
            }
        }
        binding.viewPrePage.setOnClickListener {
            val prePage = binding.viewpager.currentItem - 1
            if (prePage > -1) {
                setPage(prePage)
            }
        }
        binding.viewpager.post {
            val padding = ((binding.viewpager.width - UIUtil.dp2px(280F, this)) / 2
                    - UIUtil.dp2px(0F, this)).toInt()
            binding.viewpager.setPadding(padding, 0, padding, 0)

            // 设置页面间距
            val pageMargin = padding - UIUtil.dp2px(25F, this)//漏出的左右视图的宽度
            val pageTransformer = CompositePageTransformer()
            pageTransformer.addTransformer { page, position ->
                page.translationX = pageMargin * position
                page.scaleY = 0.8f + (1 - Math.abs(position)) * 0.2f
            }
            binding.viewpager.setPageTransformer(pageTransformer)

        }
        // 设置 padding，使左右部分可以露出
        binding.viewpager.clipToPadding = false
        binding.viewpager.clipChildren = false
        binding.viewpager.offscreenPageLimit = 3
        setData()
    }

    var openLightAnim: AnimatorSet? = null
    fun startLightAnim() {
        val lightView = binding.ivLight
        val targetView = lightView
        val scaleX = ObjectAnimator.ofFloat(targetView, View.SCALE_X, 1f, 2f)
        scaleX.duration = 200
        val scaleY = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 1f, 2f)
        scaleY.duration = 200

        val alpha = ObjectAnimator.ofFloat(targetView, View.ALPHA, 1f, 0f)
        alpha.startDelay = 200
        alpha.duration = 800

        val rotate = ObjectAnimator.ofFloat(targetView, View.ROTATION, 0f, 25f)
        rotate.duration = 800

        openLightAnim = AnimatorSet()
        openLightAnim?.playTogether(scaleX, scaleY, alpha, rotate)
        openLightAnim?.start()
    }

    val items = listOf("Page 1", "Page 2", "Page 3", "Page 4", "Page 5")
    private fun setData() {

        val viewPagerAdapter = ViewPagerAdapter(items)
        binding.viewpager.adapter = viewPagerAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }


    companion object {
        var instance: ViewPagerActivity? = null

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ViewPagerActivity::class.java)
            context.startActivity(starter)
        }
    }
}