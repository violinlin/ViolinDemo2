package com.violin.demo.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.violin.base.act.beans.DetailActivityData
import com.violin.demo.databinding.ItemMainViewLayoutBinding

class MainListAdapter(
    private val onItemClick: (DetailActivityData) -> Unit
) : ListAdapter<DetailActivityData, MainListAdapter.DemoViewHolder>(DemoDiffCallback()) {

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
