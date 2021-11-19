package com.bddrmwan.cocktailcatalogue.main.modules

import com.bddrmwan.cocktailcatalogue.main.core.datasource.RemoteDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.repository.CocktailRepositoryImpl
import com.bddrmwan.cocktailcatalogue.main.core.repository.FilterCocktailRepositoryImpl
import com.bddrmwan.cocktailcatalogue.main.core.repository.ICocktailRepository
import com.bddrmwan.cocktailcatalogue.main.core.repository.IFilterCocktailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCocktailRepository(
        remoteDataSource: RemoteDataSourceImpl
    ): ICocktailRepository {
        return CocktailRepositoryImpl(remoteDataSource)
    }

    @Singleton
    @Provides
    fun provideFilterCocktailRepository(
        remoteDataSource: RemoteDataSourceImpl
    ): IFilterCocktailRepository {
        return FilterCocktailRepositoryImpl(remoteDataSource)
    }
}