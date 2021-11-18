package com.bddrmwan.cocktailcatalogue.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bddrmwan.cocktailcatalogue.databinding.ContainerCocktailViewBinding

class CocktailAdapter : RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>() {
    private val listData = mutableListOf<String>()

    init {
        listData.add("")
        listData.add("")
        listData.add("")
        listData.add("")
        listData.add("")
        listData.add("")
        listData.add("")
        listData.add("")
        listData.add("")
        listData.add("")
    }

    inner class CocktailViewHolder(private val binding: ContainerCocktailViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setupView() {

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
        holder.setupView()
    }

    override fun getItemCount(): Int = listData.size

}