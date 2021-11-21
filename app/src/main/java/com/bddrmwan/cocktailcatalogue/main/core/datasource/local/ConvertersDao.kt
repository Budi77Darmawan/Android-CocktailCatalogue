package com.bddrmwan.cocktailcatalogue.main.core.datasource.local

import androidx.room.TypeConverter
import com.bddrmwan.cocktailcatalogue.main.core.model.Ingredient
import com.bddrmwan.cocktailcatalogue.main.core.model.ListIngredient
import com.google.gson.Gson

class ConvertersDao {
    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(str: String?): List<String>? {
        return str?.split(",")
    }

    @TypeConverter
    fun toStringFromIngredient(ingredient: List<Ingredient>): String {
        val temp = ListIngredient(ingredient)
        return Gson().toJson(temp)
    }

    @TypeConverter
    fun toIngredientFromString(str: String): List<Ingredient> {
        return Gson().fromJson(str, ListIngredient::class.java).value
    }
}