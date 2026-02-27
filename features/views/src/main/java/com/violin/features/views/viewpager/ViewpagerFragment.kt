package com.violin.features.views.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.CompositePageTransformer
import com.violin.base.act.BaseFragment
import com.violin.base.act.UIUtil
import com.violin.fretures.common.databinding.FragmentViewPagerBinding

class ViewpagerFragment : BaseFragment<FragmentViewPagerBinding>() {
    fun newInstance(): ViewsFragment {
        val TAG = "ViewpagerFragment"
        return ViewsFragment()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentViewPagerBinding {
        return FragmentViewPagerBinding.inflate(inflater)
    }

    override fun initView() {
        initVP()
    }

    val items = listOf("Page 1", "Page 2", "Page 3", "Page 4", "Page 5")
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
            val padding = ((binding.viewpager.width - UIUtil.dp2px(280F, binding.root.context)) / 2
                    - UIUtil.dp2px(0F, binding.root.context)).toInt()
            binding.viewpager.setPadding(padding, 0, padding, 0)

            // 设置页面间距
            val pageMargin = padding - UIUtil.dp2px(25F, binding.root.context)//漏出的左右视图的宽度
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

        val viewPagerAdapter = ViewPagerAdapter(items)
        binding.viewpager.adapter = viewPagerAdapter
    }

    private fun setPage(pageIndex: Int) {
        if (binding.viewpager.currentItem == pageIndex) {
            return
        }
        binding.viewpager.currentItem = pageIndex
    }
}