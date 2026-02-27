package com.violin.demo

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.violin.base.act.BaseActivity
import com.violin.base.act.beans.FeatureItemData
import com.violin.demo.databinding.ActivityMainLayoutBinding
import com.violin.demo.main.MainListAdapter
import com.violin.features.common.crash.CrashFragment
import com.violin.features.views.viewpager.ViewsFragment

class MainActivity : BaseActivity<ActivityMainLayoutBinding>() {
    override fun createBinding(inflater: LayoutInflater): ActivityMainLayoutBinding {
        return ActivityMainLayoutBinding.inflate(inflater)
    }

    private lateinit var adapter: MainListAdapter
    override fun initView() {
        adapter = MainListAdapter { demoItem ->
            if (demoItem.activityClass != null) {
                val starter = Intent(this, demoItem.activityClass?.java)
                startActivity(starter)
            } else {
                // 点击列表项，跳转到DetailActivity并传递Fragment类型
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_FRAGMENT_TYPE, demoItem.fragmentType)
                    putExtra(DetailActivity.EXTRA_TITLE, demoItem.title)
                }
                startActivity(intent)
            }

        }

        binding.recyclerview.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerview.adapter = adapter
    }

    override fun initData() {
        loadMainList()
    }

    private fun loadMainList() {
        val list = ArrayList<FeatureItemData>()
        list.add(FeatureItemData("Main", "", activityClass = MainActivity2::class))
        list.add(FeatureItemData("Crash", CrashFragment.TAG))
        list.add(FeatureItemData("Views", ViewsFragment.TAG))
        list.add(FeatureItemData("4", ""))
        list.add(FeatureItemData("5", ""))
        adapter.submitList(list)

    }
}