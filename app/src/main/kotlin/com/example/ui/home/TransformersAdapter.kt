package com.example.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.response.TransformerResponse
import com.example.databinding.ListHeaderBinding
import com.example.databinding.ListItemTransformerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

enum class TransformerType {
    AUTOBOT,
    DECEPTICON
}

class TransformersAdapter(private val transformerType: TransformerType) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(TransformersDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<TransformerResponse>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header(transformerType))
                else -> listOf(DataItem.Header(transformerType)) + list.map {
                    DataItem.TransformerItem(
                        it
                    )
                }
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

            is HeaderViewHolder -> {
                val headerItem = getItem(0) as DataItem.Header
                holder.bind(headerItem.transformerType)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.TransformerItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class HeaderViewHolder private constructor(private val binding: ListHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transformerType: TransformerType) {
            binding.addTransformerBtn.text = "New ${transformerType.name.toLowerCase().capitalize()}"
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListHeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
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

    data class Header(val transformerType: TransformerType) : DataItem() {
        override val id = "Header"
    }

    abstract val id: String
}
