package com.bddrmwan.cocktailcatalogue.main.presentation.detail.usecase

import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.repository.IBookmarkRepository
import com.bddrmwan.cocktailcatalogue.main.core.repository.ICocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IDetailUseCase {
    fun getDetailCocktail(id: String): Flow<Cocktail?>
    fun addToBookmark(cocktail: Cocktail)
    fun deleteFromBookmark(cocktail: Cocktail)
}

class DetailUseCaseImpl @Inject constructor(
    private val cocktailRepo: ICocktailRepository,
    private val bookmarkRepo: IBookmarkRepository
) : IDetailUseCase {
    override fun getDetailCocktail(id: String): Flow<Cocktail?> =
        cocktailRepo.getDetailCocktail(id)

    override fun addToBookmark(cocktail: Cocktail) =
        bookmarkRepo.addToBookmark(cocktail)

    override fun deleteFromBookmark(cocktail: Cocktail) =
        bookmarkRepo.deleteFromBookmark(cocktail)

}