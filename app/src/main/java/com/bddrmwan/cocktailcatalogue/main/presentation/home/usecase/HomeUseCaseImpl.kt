package com.bddrmwan.cocktailcatalogue.main.presentation.home.usecase

import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.core.repository.ICocktailByFilterRepository
import com.bddrmwan.cocktailcatalogue.main.core.repository.ICocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface IHomeUseCase {
    fun getCocktailByName(name: String): Flow<List<Cocktail>?>
    fun getCocktailByLetter(letter: String): Flow<List<Cocktail>?>
    fun getCocktailByFilter(filter: FilterEnum, category: String): Flow<List<Cocktail>?>
}


class HomeUseCaseImpl @Inject constructor(
    private val cocktailRepo: ICocktailRepository,
    private val cocktailFilterRepo: ICocktailByFilterRepository
) : IHomeUseCase {
    override fun getCocktailByName(name: String): Flow<List<Cocktail>?> =
        cocktailRepo.getCocktailByName(name)

    override fun getCocktailByLetter(letter: String): Flow<List<Cocktail>?> =
        cocktailRepo.getCocktailByLetter(letter)

    override fun getCocktailByFilter(filter: FilterEnum, category: String): Flow<List<Cocktail>?> =
        when (filter) {
            FilterEnum.ALCOHOLIC -> cocktailFilterRepo.getCocktailByAlcoholic(category)
            FilterEnum.GLASS -> cocktailFilterRepo.getCocktailByGlass(category)
            FilterEnum.CATEGORY -> cocktailFilterRepo.getCocktailByCategory(category)
        }
}