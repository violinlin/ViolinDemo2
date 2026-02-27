package com.violin.features.views.viewpager

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.violin.base.act.BaseFragment
import com.violin.base.act.DetailActivity
import com.violin.base.act.beans.FeatureItemData
import com.violin.features.views.ViewsListAdapter
import com.violin.fretures.common.databinding.FragmentViewsBinding

class ViewsFragment : BaseFragment<FragmentViewsBinding>() {
    companion object {
        val TAG = "ViewsFragment"
        fun newInstance(): ViewsFragment {
            return ViewsFragment()
        }
    }


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentViewsBinding {
        return FragmentViewsBinding.inflate(inflater)
    }

    private lateinit var adapter: ViewsListAdapter
    override fun initView() {
        adapter = ViewsListAdapter { demoItem ->
            if (demoItem.activityClass != null) {
                val starter = Intent(binding.root.context, demoItem.activityClass?.java)
                startActivity(starter)
            } else {
                // 点击列表项，跳转到DetailActivity并传递Fragment类型
                val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_FRAGMENT_CLASSNAME, demoItem.fragmentClassName)
                    putExtra(DetailActivity.EXTRA_TITLE, demoItem.title)
                }
                startActivity(intent)
            }

        }

        binding.recyclerview.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.recyclerview.adapter = adapter
    }

    override fun initData() {
        loadMainList()
    }

    private fun loadMainList() {
        val list = ArrayList<FeatureItemData>()
        list.add(FeatureItemData("ViewPager", ViewpagerFragment::class.java.name))
        adapter.submitList(list)

    }

}