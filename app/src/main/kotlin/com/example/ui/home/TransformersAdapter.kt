package com.example.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.R
import com.example.data.response.Transformer
import com.example.databinding.ListItemTransformerBinding
import com.example.util.Constants.TEAM_AUTOBOT
import com.example.util.Constants.TEAM_DECEPTICON
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransformersAdapter(private val clickListener: TransformerListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(TransformersDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Transformer>?) {
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
                holder.bind(transformerItem.transformerResponse, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder.from(parent)
    }

    class ItemViewHolder private constructor(private val binding: ListItemTransformerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transformer: Transformer, clickListener: TransformerListener) {
            binding.transformer = transformer
            val resId  = when(transformer.team){
                TEAM_AUTOBOT -> R.drawable.ic_transformers_autobot
                TEAM_DECEPTICON -> R.drawable.ic_transformers_decepticon
                else -> R.color.transparent
            }
            binding.transformerImage.setImageResource(resId)
            binding.clickListener = clickListener
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

class TransformerListener(val clickListener: (transformerId: String) -> Unit) {
    fun onClick(transformer: Transformer) = clickListener(transformer.id)
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
    data class TransformerItem(val transformerResponse: Transformer) : DataItem() {
        override val id = transformerResponse.id.toString()
    }

    abstract val id: String
}
