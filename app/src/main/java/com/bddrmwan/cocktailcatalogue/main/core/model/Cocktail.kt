package com.bddrmwan.cocktailcatalogue.main.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cocktail (
    val id: String,
    val name: String,
    val image: String?,
    val instructions: String?,
    val alcoholic: String?,
    val tags: List<String>?,
    val category: List<String>?,
    val ingredient: List<Ingredient>?,
): Parcelable

@Parcelize
data class Ingredient(
    val name: String,
    val measure: String? = "0"
): Parcelable