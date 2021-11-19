package com.bddrmwan.cocktailcatalogue.main.core.repository

import com.bddrmwan.cocktailcatalogue.main.core.datasource.RemoteDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface IFilterCocktailRepository {
    fun getCategoryFilterCocktailByAlcoholic(): Flow<List<FilterCocktail>?>
    fun getCategoryFilterCocktailByGlass(): Flow<List<FilterCocktail>?>
    fun getCategoryFilterCocktailByCategory(): Flow<List<FilterCocktail>?>
}


class FilterCocktailRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl
): IFilterCocktailRepository {
    override fun getCategoryFilterCocktailByAlcoholic(): Flow<List<FilterCocktail>?> {
        return flow {
            val res = remoteDataSource.getCategoryFilter(FilterEnum.ALCOHOLIC)
            res.collect {
                emit(DataMapper.mappingCategoryFilter(FilterEnum.ALCOHOLIC, it))
            }
        }
    }

    override fun getCategoryFilterCocktailByGlass(): Flow<List<FilterCocktail>?> {
        return flow {
            val res = remoteDataSource.getCategoryFilter(FilterEnum.GLASS)
            res.collect {
                emit(DataMapper.mappingCategoryFilter(FilterEnum.GLASS, it))
            }
        }
    }

    override fun getCategoryFilterCocktailByCategory(): Flow<List<FilterCocktail>?> {
        return flow {
            val res = remoteDataSource.getCategoryFilter(FilterEnum.CATEGORY)
            res.collect {
                emit(DataMapper.mappingCategoryFilter(FilterEnum.CATEGORY, it))
            }
        }
    }
}