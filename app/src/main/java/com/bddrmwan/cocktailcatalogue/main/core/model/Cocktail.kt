package com.bddrmwan.cocktailcatalogue.main.core.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Cocktail (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "instructions")
    val instructions: String?,
    @ColumnInfo(name = "alcoholic")
    val alcoholic: String?,
    @ColumnInfo(name = "glass")
    val glass: String?,
    @ColumnInfo(name = "category")
    val category: String?,
    @ColumnInfo(name = "tags")
    val tags: List<String>?,
    @ColumnInfo(name = "ingredient")
    val ingredient: List<Ingredient>?,
): Parcelable

@Parcelize
data class Ingredient(
    val name: String?,
    val measure: String? = "0"
): Parcelable

@Parcelize
data class ListIngredient(
    val value: List<Ingredient>
): Parcelable