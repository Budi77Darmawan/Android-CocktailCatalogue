package com.bddrmwan.cocktailcatalogue.main.core.repository

import com.bddrmwan.cocktailcatalogue.main.core.datasource.RemoteDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ICocktailRepository {
    fun getCocktailByName(name: String): Flow<List<Cocktail>?>
    fun getCocktailByLetter(letter: String): Flow<List<Cocktail>?>
}

class CocktailRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl
) : ICocktailRepository {
    override fun getCocktailByName(name: String): Flow<List<Cocktail>?> {
        return flow {
            val result = remoteDataSource.getCocktailByName(name)
            result.collect {
                emit(DataMapper.mappingResultCocktailToListCocktail(it))
            }
        }
    }

    override fun getCocktailByLetter(letter: String): Flow<List<Cocktail>?> {
        return flow {
            val result = remoteDataSource.getCocktailByLetter(letter)
            result.collect {
                emit(DataMapper.mappingResultCocktailToListCocktail(it))
            }
        }
    }
}