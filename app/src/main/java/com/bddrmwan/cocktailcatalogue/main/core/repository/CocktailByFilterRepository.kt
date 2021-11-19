package com.bddrmwan.cocktailcatalogue.main.core.repository

import com.bddrmwan.cocktailcatalogue.main.core.datasource.RemoteDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface ICocktailByFilterRepository {
    fun getCocktailByAlcoholic(category: String): Flow<List<Cocktail>?>
    fun getCocktailByGlass(category: String): Flow<List<Cocktail>?>
    fun getCocktailByCategory(category: String): Flow<List<Cocktail>?>
}

class CocktailByFilterRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl
) : ICocktailByFilterRepository {
    override fun getCocktailByAlcoholic(category: String): Flow<List<Cocktail>?> {
        return flow {
            val result = remoteDataSource.getCocktailByFilter(FilterEnum.ALCOHOLIC, category)
            result.collect {
                emit(DataMapper.mappingResultCocktailToListCocktail(it))
            }
        }
    }

    override fun getCocktailByGlass(category: String): Flow<List<Cocktail>?> {
        return flow {
            val result = remoteDataSource.getCocktailByFilter(FilterEnum.GLASS, category)
            result.collect {
                emit(DataMapper.mappingResultCocktailToListCocktail(it))
            }
        }
    }

    override fun getCocktailByCategory(category: String): Flow<List<Cocktail>?> {
        return flow {
            val result = remoteDataSource.getCocktailByFilter(FilterEnum.CATEGORY, category)
            result.collect {
                emit(DataMapper.mappingResultCocktailToListCocktail(it))
            }
        }
    }

}