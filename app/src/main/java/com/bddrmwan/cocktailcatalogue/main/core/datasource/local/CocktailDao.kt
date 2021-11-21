package com.bddrmwan.cocktailcatalogue.main.core.datasource.local

import androidx.room.*
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToBookmark(cocktail: Cocktail)

    @Delete
    fun deleteFromBookmark(cocktail: Cocktail)

    @Query("SELECT * from cocktail ORDER BY name ASC")
    fun getAllCocktails(): Flow<List<Cocktail>?>

    @Query("SELECT * from cocktail WHERE name LIKE :name")
    fun searchCocktailsByName(name: String): Flow<List<Cocktail>?>

    @Query("SELECT * from cocktail WHERE id = :id")
    fun getDetailCocktail(id: String): Flow<List<Cocktail>?>
}