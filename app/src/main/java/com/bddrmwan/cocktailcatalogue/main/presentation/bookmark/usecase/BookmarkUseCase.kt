package com.bddrmwan.cocktailcatalogue.main.presentation.bookmark.usecase

import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.repository.IBookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IBookmarkUseCase {
    fun searchCocktailsByName(name: String): Flow<List<Cocktail>?>
    fun getAllCocktails(): Flow<List<Cocktail>?>
}

class BookmarkUseCaseImpl @Inject constructor(
    private val bookmarkRepo: IBookmarkRepository
): IBookmarkUseCase {
    override fun searchCocktailsByName(name: String): Flow<List<Cocktail>?> =
        bookmarkRepo.searchCocktailsByName(name)

    override fun getAllCocktails(): Flow<List<Cocktail>?> =
        bookmarkRepo.getAllCocktails()
}