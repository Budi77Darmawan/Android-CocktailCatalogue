package com.bddrmwan.cocktailcatalogue.main.home.usecase

import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.repository.CocktailRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface IHomeUseCase {
    suspend fun getCocktailByName(name: String): Flow<List<Cocktail>>
    suspend fun getCocktailByLetter(letter: String): Flow<List<Cocktail>>
}


class HomeUseCase @Inject constructor(
    private val cocktailRepo: CocktailRepositoryImpl
    ): IHomeUseCase {
    override suspend fun getCocktailByName(name: String): Flow<List<Cocktail>> {
        return cocktailRepo.getCocktailByName(name)
    }

    override suspend fun getCocktailByLetter(letter: String): Flow<List<Cocktail>> {
        return cocktailRepo.getCocktailByLetter(letter)
    }
}