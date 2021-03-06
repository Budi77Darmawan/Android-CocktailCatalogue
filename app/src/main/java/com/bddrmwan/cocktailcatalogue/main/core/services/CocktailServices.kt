package com.bddrmwan.cocktailcatalogue.main.core.services

import com.bddrmwan.cocktailcatalogue.main.core.datasource.remote.ResultCocktailEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailServices {

    @GET("search.php")
    suspend fun getCocktailBySearch(
        @Query("s") name: String? = null,
        @Query("f") letter: String? = null
    ): ResultCocktailEntity

    @GET("filter.php")
    suspend fun getCocktailByFilter(
        @Query("a") alcoholic: String? = null,
        @Query("c") category: String? = null,
        @Query("g") glass: String? = null
    ): ResultCocktailEntity

    @GET("list.php")
    suspend fun getCategoryFilter(
        @Query("a") alcoholic: String? = null,
        @Query("c") category: String? = null,
        @Query("g") glass: String? = null
    ): ResultCocktailEntity

    @GET("lookup.php")
    suspend fun getDetailCocktail(
        @Query("i") id: String
    ): ResultCocktailEntity
}