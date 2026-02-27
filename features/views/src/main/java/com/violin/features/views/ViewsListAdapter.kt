package com.violin.features.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.violin.base.act.beans.DetailActivityData
import com.violin.fretures.common.databinding.ItemViewsViewLayoutBinding

class ViewsListAdapter(
    private val onItemClick: (DetailActivityData) -> Unit
) : ListAdapter<DetailActivityData, ViewsListAdapter.DemoViewHolder>(DemoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        val binding = ItemViewsViewLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DemoViewHolder(
        private val binding: ItemViewsViewLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailActivityData) {
            binding.apply {
                tvTitle.text = item.title
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    private class DemoDiffCallback : DiffUtil.ItemCallback<DetailActivityData>() {
        override fun areItemsTheSame(oldItem: DetailActivityData, newItem: DetailActivityData): Boolean {
            return oldItem.fragmentClassName == newItem.fragmentClassName
        }

        override fun areContentsTheSame(oldItem: DetailActivityData, newItem: DetailActivityData): Boolean {
            return oldItem == newItem
        }
    }
}
