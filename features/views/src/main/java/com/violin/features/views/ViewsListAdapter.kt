package com.violin.features.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.violin.base.act.beans.FeatureItemData
import com.violin.fretures.common.databinding.ItemViewsViewLayoutBinding

class ViewsListAdapter(
    private val onItemClick: (FeatureItemData) -> Unit
) : ListAdapter<FeatureItemData, ViewsListAdapter.DemoViewHolder>(DemoDiffCallback()) {

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

        fun bind(item: FeatureItemData) {
            binding.apply {
                tvTitle.text = item.title
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    private class DemoDiffCallback : DiffUtil.ItemCallback<FeatureItemData>() {
        override fun areItemsTheSame(oldItem: FeatureItemData, newItem: FeatureItemData): Boolean {
            return oldItem.fragmentType == newItem.fragmentType
        }

        override fun areContentsTheSame(oldItem: FeatureItemData, newItem: FeatureItemData): Boolean {
            return oldItem == newItem
        }
    }
}
