package com.bddrmwan.cocktailcatalogue.main.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bddrmwan.cocktailcatalogue.databinding.ContainerCocktailViewBinding
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.extensions.getProgressDrawable
import com.bddrmwan.cocktailcatalogue.main.extensions.loadImage

class CocktailAdapter(
    private val onClick: (Cocktail?) -> Unit
) : RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>() {
    private val listData = mutableListOf<Cocktail>()
    private var selectedFilter: FilterCocktail? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listData: List<Cocktail>, selectedFilter: FilterCocktail? = null) {
        this.selectedFilter = selectedFilter
        this.listData.clear()
        this.listData.addAll(listData)
        notifyDataSetChanged()
    }

    inner class CocktailViewHolder(private val binding: ContainerCocktailViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setupView(cocktail: Cocktail?) {
            binding.apply {
                tvCocktailName.text = cocktail?.name
                if (selectedFilter == null) tvInstructionCocktail.text = cocktail?.instructions
                else tvInstructionCocktail.text = selectedFilter?.name

                imgCocktail.loadImage(
                    cocktail?.image,
                    getProgressDrawable(root.context)
                )
                root.setOnClickListener { onClick.invoke(cocktail) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailViewHolder {
        return CocktailViewHolder(
            ContainerCocktailViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) {
        val data = listData.getOrNull(position)
        holder.setupView(data)
    }

    override fun getItemCount(): Int = listData.size

}