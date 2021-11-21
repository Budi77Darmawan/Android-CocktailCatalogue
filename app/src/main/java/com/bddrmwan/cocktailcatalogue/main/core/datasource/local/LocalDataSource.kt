package com.bddrmwan.cocktailcatalogue.main.core.datasource.local

import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


class LocalDataSourceImpl @Inject constructor(
    private val database: CocktailRoomDatabase
) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun addToBookmark(cocktail: Cocktail) {
        executorService.execute {
            database.cocktailDao().addToBookmark(cocktail)
        }
    }

    fun getDetailCocktail(id: String): Flow<List<Cocktail>?> {
        return database.cocktailDao().getDetailCocktail(id)
    }
}