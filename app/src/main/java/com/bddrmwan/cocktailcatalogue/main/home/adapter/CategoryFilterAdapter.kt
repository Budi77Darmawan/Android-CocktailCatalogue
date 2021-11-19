package com.bddrmwan.cocktailcatalogue.main.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.databinding.ContainerCategoryFilterBinding
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail

class CategoryFilterAdapter(
    private val onSelected: (FilterCocktail?) -> Unit
) : RecyclerView.Adapter<CategoryFilterAdapter.FilterViewHolder>() {
    private val listData = mutableListOf<FilterCocktail>()
    private var defaultSelectedFilter: FilterCocktail? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listData: List<FilterCocktail>?, defaultSelected: FilterCocktail? = null) {
        this.defaultSelectedFilter = defaultSelected
        this.listData.clear()
        this.listData.addAll(listData ?: listOf())
        notifyDataSetChanged()
    }

    inner class FilterViewHolder(
        private val binding: ContainerCategoryFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setupView(category: FilterCocktail?) {
            binding.apply {
                if (category?.isSelected == true || category?.name?.equals(defaultSelectedFilter?.name) == true) {
                    root.background = ContextCompat.getDrawable(
                        root.context,
                        R.drawable.bg_selected_rounded_blue_button
                    )
                } else {
                    root.background = ContextCompat.getDrawable(
                        root.context,
                        R.drawable.bg_unselected_rounded_blue_button
                    )
                }

                tvCategoryName.text = category?.name
                root.setOnClickListener {
                    onSelected.invoke(category)
                }
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
        val category = listData.getOrNull(position)
        holder.setupView(category)
    }

    override fun getItemCount(): Int = listData.size


}