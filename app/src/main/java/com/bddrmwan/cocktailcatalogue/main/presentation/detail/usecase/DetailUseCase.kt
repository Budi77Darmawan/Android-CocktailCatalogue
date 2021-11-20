package com.bddrmwan.cocktailcatalogue.main.presentation.detail.usecase

import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.repository.ICocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IDetailUseCase {
    fun getDetailCocktail(id: String): Flow<Cocktail?>
}

class DetailUseCaseImpl @Inject constructor(
    private val cocktailRepo: ICocktailRepository
) : IDetailUseCase {
    override fun getDetailCocktail(id: String): Flow<Cocktail?> =
        cocktailRepo.getDetailCocktail(id)

}