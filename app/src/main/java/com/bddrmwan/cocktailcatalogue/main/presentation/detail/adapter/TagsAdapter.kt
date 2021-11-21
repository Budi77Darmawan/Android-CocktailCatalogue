package com.bddrmwan.cocktailcatalogue.main.presentation.detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bddrmwan.cocktailcatalogue.databinding.ContainerCategoryFilterBinding

class TagsAdapter: RecyclerView.Adapter<TagsAdapter.FilterViewHolder>() {
    private val listData = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listData: List<String>) {
        this.listData.clear()
        this.listData.addAll(listData)
        notifyDataSetChanged()
    }

    inner class FilterViewHolder(
        private val binding: ContainerCategoryFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setupView(tag: String?) {
            binding.apply {
                tvCategoryName.text = tag
                tvCategoryName.textSize = 12.0f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            ContainerCategoryFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val tag = listData.getOrNull(position)
        holder.setupView(tag)
    }

    override fun getItemCount(): Int = listData.size


}