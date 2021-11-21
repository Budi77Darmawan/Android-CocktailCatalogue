package com.bddrmwan.cocktailcatalogue.main.core.repository

import com.bddrmwan.cocktailcatalogue.main.core.datasource.local.LocalDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface IBookmarkRepository {
    fun addToBookmark(cocktail: Cocktail)
    fun deleteFromBookmark(cocktail: Cocktail)
    fun getAllCocktails(): Flow<List<Cocktail>?>
    fun getDetailCocktail(id: String): Flow<Cocktail?>
}

class BookmarkRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSourceImpl
) : IBookmarkRepository {

    override fun addToBookmark(cocktail: Cocktail) {
        localDataSource.addToBookmark(cocktail)
    }

    override fun deleteFromBookmark(cocktail: Cocktail) {
        localDataSource.deleteFromBookmark(cocktail)
    }

    override fun getAllCocktails(): Flow<List<Cocktail>?> {
        return flow {
            val result = localDataSource.getAllCocktails()
            result.collect { emit(it) }
        }
    }

    override fun getDetailCocktail(id: String): Flow<Cocktail?> {
        return flow {
            val result = localDataSource.getDetailCocktail(id)
            result.collect { emit(it?.getOrNull(0)) }
        }
    }
}