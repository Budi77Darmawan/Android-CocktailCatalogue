package com.bddrmwan.cocktailcatalogue.main.modules

import com.bddrmwan.cocktailcatalogue.main.core.repository.CocktailByFilterRepositoryImpl
import com.bddrmwan.cocktailcatalogue.main.core.repository.CocktailRepositoryImpl
import com.bddrmwan.cocktailcatalogue.main.core.repository.FilterCocktailRepositoryImpl
import com.bddrmwan.cocktailcatalogue.main.presentation.home.usecase.FilterUseCaseImpl
import com.bddrmwan.cocktailcatalogue.main.presentation.home.usecase.HomeUseCaseImpl
import com.bddrmwan.cocktailcatalogue.main.presentation.home.usecase.IFilterUseCase
import com.bddrmwan.cocktailcatalogue.main.presentation.home.usecase.IHomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideHomeUseCase(
        cocktailRepo: CocktailRepositoryImpl,
        cocktailFilterRepo: CocktailByFilterRepositoryImpl
    ): IHomeUseCase {
        return HomeUseCaseImpl(cocktailRepo, cocktailFilterRepo)
    }

    @Singleton
    @Provides
    fun provideFilterUseCase(
        filterCocktailRepo: FilterCocktailRepositoryImpl
    ): IFilterUseCase {
        return FilterUseCaseImpl(filterCocktailRepo)
    }
    
}