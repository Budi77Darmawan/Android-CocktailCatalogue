package com.bddrmwan.cocktailcatalogue.main.modules

import com.bddrmwan.cocktailcatalogue.main.core.repository.CocktailRepositoryImpl
import com.bddrmwan.cocktailcatalogue.main.home.usecase.HomeUseCase
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
    ): HomeUseCase {
        return HomeUseCase(cocktailRepo)
    }
}