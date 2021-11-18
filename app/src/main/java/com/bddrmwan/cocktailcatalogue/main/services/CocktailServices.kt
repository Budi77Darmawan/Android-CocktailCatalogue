package com.bddrmwan.cocktailcatalogue.main.services

import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailServices {

    @GET("search.php")
    suspend fun getCocktailByQuery(
        @Query("s") name: String? = null,
        @Query("f") letter: String? = null
    )

    @GET("filter.php")
    suspend fun getCocktailByFilter(
        @Query("a") alcoholic: String? = null,
        @Query("c") category: String? = null,
        @Query("g") glass: String? = null
    )
}