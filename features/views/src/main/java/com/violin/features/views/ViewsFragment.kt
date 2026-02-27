package com.violin.features.views.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.violin.base.act.BaseFragment
import com.violin.base.act.SchemeUtils
import com.violin.base.act.beans.DetailActivityData
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
            activity?.let {
                SchemeUtils.openDetailActivity(it, demoItem)
            }
        }

        binding.recyclerview.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.recyclerview.adapter = adapter
    }

    override fun initData() {
        loadMainList()
    }

    private fun loadMainList() {
        val list = ArrayList<DetailActivityData>()
        list.add(DetailActivityData("ViewPager", ViewpagerFragment::class.java.name))
        adapter.submitList(list)

    }

}