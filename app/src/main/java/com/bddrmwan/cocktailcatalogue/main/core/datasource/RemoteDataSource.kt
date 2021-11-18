package com.bddrmwan.cocktailcatalogue.main.core.datasource

import com.bddrmwan.cocktailcatalogue.main.services.CocktailServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class RemoteDataSourceImpl @Inject constructor(
    private val services: CocktailServices
) {

    suspend fun getCocktailByName(name: String): Flow<ResultCocktailEntity> {
        return flow {
            val response = services.getCocktailBySearch(name = name)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCocktailByLetter(letter: String): Flow<ResultCocktailEntity> {
        return flow {
            val response = services.getCocktailBySearch(letter = letter)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}