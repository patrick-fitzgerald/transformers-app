package com.example.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.response.TransformerResponse
import com.example.databinding.ListItemTransformerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TransformersAdapter :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(TransformersDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<TransformerResponse>?) {
        adapterScope.launch {
            val items = list?.map {
                DataItem.TransformerItem(it)
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val transformerItem = getItem(position) as DataItem.TransformerItem
                holder.bind(transformerItem.transformerResponse)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder.from(parent)
    }

    class ItemViewHolder private constructor(private val binding: ListItemTransformerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transformer: TransformerResponse) {
            binding.transformer = transformer
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTransformerBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }
}

class TransformersDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class TransformerItem(val transformerResponse: TransformerResponse) : DataItem() {
        override val id = transformerResponse.id.toString()
    }

    abstract val id: String
}
