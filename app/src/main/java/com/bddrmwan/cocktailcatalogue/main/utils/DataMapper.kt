package com.bddrmwan.cocktailcatalogue.main.utils

import com.bddrmwan.cocktailcatalogue.main.core.datasource.CocktailEntity
import com.bddrmwan.cocktailcatalogue.main.core.datasource.ResultCocktailEntity
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.core.model.Ingredient

object DataMapper {
    fun mappingResultCocktailToListCocktail(
        data: ResultCocktailEntity
    ): List<Cocktail>? {
        return data.drinks?.map {
            Cocktail(
                it.idDrink,
                it.strDrink,
                it.strDrinkThumb,
                it.strInstructions,
                it.strAlcoholic,
                it.strGlass,
                it.strCategory,
                mappingStringToList(it.strTags),
                mappingIngredient(it)
            )
        }
    }

    fun mappingCategoryFilter(
        filter: FilterEnum, data: ResultCocktailEntity
    ): List<FilterCocktail>? {
        return when (filter) {
            FilterEnum.ALCOHOLIC -> {
                data.drinks?.map { FilterCocktail(it.strAlcoholic ?: "", filter) }
                    ?.filterNot { str -> str.name == "" }
            }
            FilterEnum.GLASS -> {
                data.drinks?.map { FilterCocktail(it.strGlass ?: "", filter) }
                    ?.filterNot { str -> str.name == "" }
            }
            FilterEnum.CATEGORY -> {
                data.drinks?.map { FilterCocktail(it.strCategory ?: "", filter) }
                    ?.filterNot { str -> str.name == "" }
            }
        }
    }

    private fun mappingStringToList(str: String?): List<String>? {
        return str?.split(",")
    }

    private fun mappingIngredient(cocktail: CocktailEntity): List<Ingredient> {
        val listIngredient = mutableListOf<Ingredient>()
        listIngredient.add(Ingredient(cocktail.strIngredient1, cocktail.strMeasure1))
        listIngredient.add(Ingredient(cocktail.strIngredient2, cocktail.strMeasure2))
        listIngredient.add(Ingredient(cocktail.strIngredient3, cocktail.strMeasure3))
        listIngredient.add(Ingredient(cocktail.strIngredient4, cocktail.strMeasure4))
        listIngredient.add(Ingredient(cocktail.strIngredient5, cocktail.strMeasure5))
        listIngredient.add(Ingredient(cocktail.strIngredient6, cocktail.strMeasure6))
        listIngredient.add(Ingredient(cocktail.strIngredient7, cocktail.strMeasure7))
        listIngredient.add(Ingredient(cocktail.strIngredient8, cocktail.strMeasure8))
        listIngredient.add(Ingredient(cocktail.strIngredient9, cocktail.strMeasure9))
        listIngredient.add(Ingredient(cocktail.strIngredient10, cocktail.strMeasure10))
        listIngredient.add(Ingredient(cocktail.strIngredient11, cocktail.strMeasure11))
        listIngredient.add(Ingredient(cocktail.strIngredient12, cocktail.strMeasure12))
        listIngredient.add(Ingredient(cocktail.strIngredient13, cocktail.strMeasure13))
        listIngredient.add(Ingredient(cocktail.strIngredient14, cocktail.strMeasure14))
        listIngredient.add(Ingredient(cocktail.strIngredient15, cocktail.strMeasure15))
        return listIngredient.filterNot { it.name == null || it.measure == null }
    }
}