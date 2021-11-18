package com.bddrmwan.cocktailcatalogue.main.home.usecase

import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.repository.ICocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface IHomeUseCase {
    fun getCocktailByName(name: String): Flow<List<Cocktail>?>
    fun getCocktailByLetter(letter: String): Flow<List<Cocktail>?>
}


class HomeUseCase @Inject constructor(
    private val cocktailRepo: ICocktailRepository
) : IHomeUseCase {
    override fun getCocktailByName(name: String): Flow<List<Cocktail>?> =
        cocktailRepo.getCocktailByName(name)

    override fun getCocktailByLetter(letter: String): Flow<List<Cocktail>?> =
        cocktailRepo.getCocktailByLetter(letter)
}