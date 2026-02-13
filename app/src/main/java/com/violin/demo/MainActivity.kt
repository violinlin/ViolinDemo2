package com.violin.demo

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.violin.base.act.BaseActivity
import com.violin.demo.databinding.ActivityMainLayoutBinding
import com.violin.demo.main.MainItemData
import com.violin.demo.main.MainListAdapter
import com.violin.features.common.crash.CrashFragment

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

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }

    override fun initData() {
        loadMainList()
    }

    private fun loadMainList() {
        val list = ArrayList<MainItemData>()
        list.add(MainItemData("Main", "", activityClass = MainActivity2::class))
        list.add(MainItemData("Crash", CrashFragment.TAG))
        list.add(MainItemData("3", ""))
        list.add(MainItemData("4", ""))
        list.add(MainItemData("5", ""))
        adapter.submitList(list)

    }
}