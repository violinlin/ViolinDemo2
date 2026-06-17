package com.violin.features.views.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.violin.fretures.common.databinding.ItemRecyclerviewTestBinding

class RecyclerViewTestAdapter(
    private val onItemLongClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerViewTestAdapter.ItemViewHolder>() {

    private val items = ArrayList<RecyclerViewTestItem>()

    fun setData(data: List<RecyclerViewTestItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: List<RecyclerViewTestItem>) {
        val startPosition = items.size
        items.addAll(data)
        notifyItemRangeInserted(startPosition, data.size)
    }

    fun addFrontData(data: List<RecyclerViewTestItem>) {
        if (data.isEmpty()) return
        items.addAll(0, data)
        notifyItemRangeInserted(0, data.size)
//        notifyDataSetChanged()
    }

    fun removeTail(count: Int) {
        if (count <= 0) return
        val startPosition = items.size - count
        repeat(count) {
            items.removeAt(items.lastIndex)
        }
        notifyItemRangeRemoved(startPosition, count)
    }

    fun removeFront(count: Int) {
        if (count <= 0) return
        repeat(count) {
            items.removeAt(0)
        }
        notifyItemRangeRemoved(0, count)
    }

    fun removeAt(position: Int): Boolean {
        if (position !in items.indices) return false
        items.removeAt(position)
        notifyItemRemoved(position)
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRecyclerviewTestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(
        private val binding: ItemRecyclerviewTestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecyclerViewTestItem, bindPosition: Int) {
            binding.tvTitle.text = "#${item.id} ${item.title}"
            binding.tvDesc.text = item.description
            binding.tvCreatedAt.text = "创建时间：${item.createdAt}"
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Toast.makeText(
                        binding.root.context,
                        "bindingAdapterPosition：$position\nbind position：$bindPosition",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            binding.root.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClick(position)
                }
                true
            }
        }
    }
}

data class RecyclerViewTestItem(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: String
)
