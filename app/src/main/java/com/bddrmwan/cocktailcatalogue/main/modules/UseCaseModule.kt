package com.bddrmwan.cocktailcatalogue.main.modules

import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.core.repository.CocktailRepositoryImpl
import com.bddrmwan.cocktailcatalogue.main.core.repository.FilterCocktailRepositoryImpl
import com.bddrmwan.cocktailcatalogue.main.home.usecase.FilterUseCase
import com.bddrmwan.cocktailcatalogue.main.home.usecase.HomeUseCase
import com.bddrmwan.cocktailcatalogue.main.home.usecase.IFilterUseCase
import com.bddrmwan.cocktailcatalogue.main.home.usecase.IHomeUseCase
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
        cocktailRepo: CocktailRepositoryImpl
    ): IHomeUseCase {
        return HomeUseCase(cocktailRepo)
    }

    @Singleton
    @Provides
    fun provideFilterUseCase(
        filterCocktailRepo: FilterCocktailRepositoryImpl
    ): IFilterUseCase {
        return FilterUseCase(filterCocktailRepo)
    }
}