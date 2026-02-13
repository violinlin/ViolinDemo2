package com.violin.demo.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.violin.demo.databinding.ItemMainViewLayoutBinding

class MainListAdapter(
    private val onItemClick: (MainItemData) -> Unit
) : ListAdapter<MainItemData, MainListAdapter.DemoViewHolder>(DemoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        val binding = ItemMainViewLayoutBinding.inflate(
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
        private val binding: ItemMainViewLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MainItemData) {
            binding.apply {
                tvTitle.text = item.title
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    private class DemoDiffCallback : DiffUtil.ItemCallback<MainItemData>() {
        override fun areItemsTheSame(oldItem: MainItemData, newItem: MainItemData): Boolean {
            return oldItem.fragmentType == newItem.fragmentType
        }

        override fun areContentsTheSame(oldItem: MainItemData, newItem: MainItemData): Boolean {
            return oldItem == newItem
        }
    }
}
