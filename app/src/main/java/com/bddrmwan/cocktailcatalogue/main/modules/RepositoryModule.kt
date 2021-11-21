package com.bddrmwan.cocktailcatalogue.main.modules

import com.bddrmwan.cocktailcatalogue.main.core.datasource.local.LocalDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.datasource.remote.RemoteDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.repository.*
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


    @Singleton
    @Provides
    fun provideCocktailByFilterRepository(
        remoteDataSource: RemoteDataSourceImpl
    ): ICocktailByFilterRepository {
        return CocktailByFilterRepositoryImpl(remoteDataSource)
    }
}