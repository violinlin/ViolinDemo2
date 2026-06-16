package com.violin.features.views.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.violin.fretures.common.databinding.ItemRecyclerviewTestBinding

class RecyclerViewTestAdapter(
    private val onItemLongClick: (RecyclerViewTestItem) -> Unit
) : ListAdapter<RecyclerViewTestItem, RecyclerViewTestAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRecyclerviewTestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(
        private val binding: ItemRecyclerviewTestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecyclerViewTestItem) {
            binding.tvTitle.text = "#${item.id} ${item.title}"
            binding.tvDesc.text = item.description
            binding.tvCreatedAt.text = "创建时间：${item.createdAt}"
            binding.root.setOnLongClickListener {
                onItemLongClick(item)
                true
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<RecyclerViewTestItem>() {
        override fun areItemsTheSame(
            oldItem: RecyclerViewTestItem,
            newItem: RecyclerViewTestItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecyclerViewTestItem,
            newItem: RecyclerViewTestItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}

data class RecyclerViewTestItem(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: String
)
