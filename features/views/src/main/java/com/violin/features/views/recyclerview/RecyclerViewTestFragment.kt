package com.violin.features.views.recyclerview

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.violin.base.act.BaseFragment
import com.violin.fretures.common.databinding.FragmentRecyclerviewTestBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecyclerViewTestFragment : BaseFragment<FragmentRecyclerviewTestBinding>() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private val adapter = RecyclerViewTestAdapter(::deleteItem)
    private val items = ArrayList<RecyclerViewTestItem>()
    private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private var nextId = 1L
    private var batchIndex = 1
    private var isLoadingMore = false
    private var hasMore = true

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecyclerviewTestBinding {
        return FragmentRecyclerviewTestBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
        binding.recyclerview.addOnScrollListener(loadMoreScrollListener)

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        binding.btnBatchAdd.setOnClickListener {
            appendItems(BATCH_SIZE)
        }
        binding.btnFrontAdd.setOnClickListener {
            prependItems(BATCH_SIZE)
        }
        binding.btnFrontDelete.setOnClickListener {
            deleteFrontItems(BATCH_SIZE)
        }
        binding.btnBatchDelete.setOnClickListener {
            deleteTailItems(BATCH_SIZE)
        }
    }

    override fun initData() {
        resetData()
    }

    override fun onDestroyView() {
        binding.recyclerview.removeOnScrollListener(loadMoreScrollListener)
        mainHandler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    private val loadMoreScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy <= 0 || isLoadingMore || !hasMore) return

            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
            val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
            if (lastVisiblePosition >= adapter.itemCount - LOAD_MORE_THRESHOLD) {
                loadMore()
            }
        }
    }

    private fun refreshData() {
        showLoadState("")
        mainHandler.postDelayed({
            if (view == null) return@postDelayed
            resetData()
            binding.swipeRefreshLayout.isRefreshing = false
        }, REFRESH_DELAY_MILLIS)
    }

    private fun loadMore() {
        if (!hasMore) {
            showLoadState("没有更多数据")
            return
        }

        isLoadingMore = true
        showLoadState("加载中...")
        mainHandler.postDelayed({
            if (view == null) return@postDelayed
            isLoadingMore = false
            appendItems(BATCH_SIZE)
        }, LOAD_MORE_DELAY_MILLIS)
    }

    private fun resetData() {
        nextId = 1L
        batchIndex = 1
        hasMore = true
        isLoadingMore = false
        items.clear()
        items.addAll(createItems(INITIAL_SIZE))
        adapter.setData(items)
        submitCurrentItems()
    }

    private fun appendItems(count: Int) {
        if (items.size >= MAX_ITEMS) {
            hasMore = false
            submitCurrentItems()
            return
        }

        val addCount = minOf(count, MAX_ITEMS - items.size)
        val newItems = createItems(addCount)
        items.addAll(newItems)
        adapter.addData(newItems)
        hasMore = items.size < MAX_ITEMS
        submitCurrentItems()
    }

    private fun prependItems(count: Int) {
        if (items.size >= MAX_ITEMS) {
            hasMore = false
            submitCurrentItems()
            return
        }

        val addCount = minOf(count, MAX_ITEMS - items.size)
        val newItems = createItems(addCount)
        items.addAll(0, newItems)
        adapter.addFrontData(newItems)
        hasMore = items.size < MAX_ITEMS
        submitCurrentItems()
    }

    private fun deleteTailItems(count: Int) {
        val deleteCount = minOf(count, items.size)
        repeat(deleteCount) {
            items.removeAt(items.lastIndex)
        }
        adapter.removeTail(deleteCount)
        hasMore = true
        isLoadingMore = false
        submitCurrentItems()
    }

    private fun deleteFrontItems(count: Int) {
        val deleteCount = minOf(count, items.size)
        repeat(deleteCount) {
            items.removeAt(0)
        }
        adapter.removeFront(deleteCount)
        hasMore = true
        isLoadingMore = false
        submitCurrentItems()
    }

    private fun deleteItem(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            adapter.removeAt(position)
            hasMore = true
            submitCurrentItems()
        }
    }

    private fun createItems(count: Int): List<RecyclerViewTestItem> {
        val currentBatch = batchIndex++
        return List(count) {
            val id = nextId++
            RecyclerViewTestItem(
                id = id,
                title = "RecyclerView Item",
                description = "第 $currentBatch 批数据，长按可以删除这一条。",
                createdAt = timeFormatter.format(Date())
            )
        }
    }

    private fun submitCurrentItems() {
        binding.tvEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        binding.btnBatchDelete.isEnabled = items.isNotEmpty()
        binding.tvSummary.text = "当前 ${items.size} 条，最多 $MAX_ITEMS 条"

        when {
            isLoadingMore -> showLoadState("加载中...")
            !hasMore -> showLoadState("没有更多数据")
            else -> showLoadState("")
        }
    }

    private fun showLoadState(text: String) {
        binding.tvLoadState.text = text
        binding.tvLoadState.visibility = if (text.isBlank()) View.GONE else View.VISIBLE
    }

    companion object {
        private const val INITIAL_SIZE = 20
        private const val BATCH_SIZE = 10
        private const val MAX_ITEMS = 100
        private const val LOAD_MORE_THRESHOLD = 3
        private const val REFRESH_DELAY_MILLIS = 600L
        private const val LOAD_MORE_DELAY_MILLIS = 500L
    }
}
