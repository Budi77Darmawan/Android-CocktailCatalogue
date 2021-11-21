package com.bddrmwan.cocktailcatalogue.main.modules

import com.bddrmwan.cocktailcatalogue.BuildConfig.BASE_URL
import com.bddrmwan.cocktailcatalogue.main.core.services.CocktailServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideService(
        client: OkHttpClient
    ): CocktailServices =
        NetworkModule.buildRetrofit(BASE_URL, client)
            .create(CocktailServices::class.java)

}