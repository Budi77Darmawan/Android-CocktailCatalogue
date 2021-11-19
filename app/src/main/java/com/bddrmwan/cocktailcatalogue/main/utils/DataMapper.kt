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
                mappingStringToList(it.strTags),
                mappingStringToList(it.strCategory),
                mappingIngredient(it)
            )
        }
    }

    fun mappingCategoryFilter(
        filter: FilterEnum, data: ResultCocktailEntity
    ): List<FilterCocktail>? {
        return when (filter) {
            FilterEnum.ALCOHOLIC -> {
                data.drinks?.map { FilterCocktail(it.strAlcoholic ?: "") }
                    ?.filterNot { str -> str.name == "" }
            }
            FilterEnum.GLASS -> {
                data.drinks?.map { FilterCocktail(it.strGlass ?: "") }
                    ?.filterNot { str -> str.name == "" }
            }
            FilterEnum.CATEGORY -> {
                data.drinks?.map { FilterCocktail(it.strCategory ?: "") }
                    ?.filterNot { str -> str.name == "" }
            }
        }
    }

    private fun mappingStringToList(str: String?): List<String>? {
        return str?.split(",")
    }

    private fun mappingIngredient(cocktail: CocktailEntity): List<Ingredient>? {
        val ingredient: MutableList<Ingredient>? = null
        cocktail.strIngredient1?.let {
            ingredient?.add(
                Ingredient(
                    cocktail.strIngredient1,
                    cocktail.strMeasure1
                )
            )
            cocktail.strIngredient2?.let {
                ingredient?.add(
                    Ingredient(
                        cocktail.strIngredient2,
                        cocktail.strMeasure2
                    )
                )
                cocktail.strIngredient3?.let {
                    ingredient?.add(
                        Ingredient(
                            cocktail.strIngredient3,
                            cocktail.strMeasure3
                        )
                    )
                    cocktail.strIngredient4?.let {
                        ingredient?.add(
                            Ingredient(
                                cocktail.strIngredient4,
                                cocktail.strMeasure4
                            )
                        )
                        cocktail.strIngredient5?.let {
                            ingredient?.add(
                                Ingredient(
                                    cocktail.strIngredient5,
                                    cocktail.strMeasure5
                                )
                            )
                            cocktail.strIngredient6?.let {
                                ingredient?.add(
                                    Ingredient(
                                        cocktail.strIngredient6,
                                        cocktail.strMeasure6
                                    )
                                )
                                cocktail.strIngredient7?.let {
                                    ingredient?.add(
                                        Ingredient(
                                            cocktail.strIngredient7,
                                            cocktail.strMeasure7
                                        )
                                    )
                                    cocktail.strIngredient8?.let {
                                        ingredient?.add(
                                            Ingredient(
                                                cocktail.strIngredient8,
                                                cocktail.strMeasure8
                                            )
                                        )
                                        cocktail.strIngredient9?.let {
                                            ingredient?.add(
                                                Ingredient(
                                                    cocktail.strIngredient9,
                                                    cocktail.strMeasure9
                                                )
                                            )
                                            cocktail.strIngredient10?.let {
                                                ingredient?.add(
                                                    Ingredient(
                                                        cocktail.strIngredient10,
                                                        cocktail.strMeasure10
                                                    )
                                                )
                                                cocktail.strIngredient11?.let {
                                                    ingredient?.add(
                                                        Ingredient(
                                                            cocktail.strIngredient11,
                                                            cocktail.strMeasure11
                                                        )
                                                    )
                                                    cocktail.strIngredient12?.let {
                                                        ingredient?.add(
                                                            Ingredient(
                                                                cocktail.strIngredient12,
                                                                cocktail.strMeasure12
                                                            )
                                                        )
                                                        cocktail.strIngredient13?.let {
                                                            ingredient?.add(
                                                                Ingredient(
                                                                    cocktail.strIngredient13,
                                                                    cocktail.strMeasure13
                                                                )
                                                            )
                                                            cocktail.strIngredient14?.let {
                                                                ingredient?.add(
                                                                    Ingredient(
                                                                        cocktail.strIngredient14,
                                                                        cocktail.strMeasure14
                                                                    )
                                                                )
                                                                cocktail.strIngredient15?.let {
                                                                    ingredient?.add(
                                                                        Ingredient(
                                                                            cocktail.strIngredient15,
                                                                            cocktail.strMeasure15
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ingredient
    }
}