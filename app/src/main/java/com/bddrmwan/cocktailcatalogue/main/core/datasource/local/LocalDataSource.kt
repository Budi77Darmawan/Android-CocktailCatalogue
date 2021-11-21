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
        cocktail.isBookmark = true
        executorService.execute {
            database.cocktailDao().addToBookmark(cocktail)
        }
    }

    fun deleteFromBookmark(cocktail: Cocktail) {
        executorService.execute {
            database.cocktailDao().deleteFromBookmark(cocktail)
        }
    }

    fun getAllCocktails(): Flow<List<Cocktail>?> {
        return database.cocktailDao().getAllCocktails()
    }

    fun getDetailCocktail(id: String): Flow<List<Cocktail>?> {
        return database.cocktailDao().getDetailCocktail(id)
    }

    fun searchCocktailByName(name: String): Flow<List<Cocktail>?> {
        val nameStr = "%$name%"
        return database.cocktailDao().searchCocktailsByName(nameStr)
    }
}