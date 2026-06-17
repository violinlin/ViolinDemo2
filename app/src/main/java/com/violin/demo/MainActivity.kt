package com.violin.demo

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.violin.base.act.BaseActivity
import com.violin.base.act.DetailActivity
import com.violin.base.act.SchemeUtils
import com.violin.base.act.beans.DetailActivityData
import com.violin.demo.databinding.ActivityMainLayoutBinding
import com.violin.demo.main.MainListAdapter
import com.violin.features.common.crash.CrashFragment
import com.violin.features.common.gift.GiftAnimationDemoFragment
import com.violin.features.common.image.ImagePickerDemoFragment
import com.violin.features.common.json.MoshiDemoFragment
import com.violin.features.common.network.NetworkTestFragment
import com.violin.features.common.network.OkHttpLearningFragment
import com.violin.features.common.proto.ProtoDemoFragment
import com.violin.features.common.string.StringFragment
import com.violin.features.views.recyclerview.RecyclerViewTestFragment
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
                SchemeUtils.openDetailActivity(this, demoItem)
            }

        }

        binding.recyclerview.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerview.adapter = adapter
    }

    override fun initData() {
        loadMainList()
    }

    private fun loadMainList() {
        val list = ArrayList<DetailActivityData>()
        list.add(DetailActivityData("Main", "", activityClass = MainActivity2::class))
        list.add(DetailActivityData("Crash", CrashFragment::class.java.name))
        list.add(DetailActivityData("Views", ViewsFragment::class.java.name))
        list.add(DetailActivityData("Strings", StringFragment::class.java.name))
        list.add(DetailActivityData("ProtoBuf Demo", ProtoDemoFragment::class.java.name))
        list.add(DetailActivityData("Moshi JSON 解析", MoshiDemoFragment::class.java.name))
        list.add(DetailActivityData("网络测试", NetworkTestFragment::class.java.name))
        list.add(DetailActivityData("OkHttp 学习", OkHttpLearningFragment::class.java.name))
        list.add(DetailActivityData("RecyclerView 测试", RecyclerViewTestFragment::class.java.name))
        list.add(DetailActivityData("图片选择预览", ImagePickerDemoFragment::class.java.name))
        list.add(DetailActivityData("送礼动画Demo", GiftAnimationDemoFragment::class.java.name))
        list.add(DetailActivityData("Compose Learning", ComposeLearningFragment::class.java.name))
        adapter.submitList(list)

    }
}
