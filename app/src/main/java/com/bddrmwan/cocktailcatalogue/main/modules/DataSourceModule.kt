package com.bddrmwan.cocktailcatalogue.main.modules

import android.content.Context
import androidx.room.Room
import com.bddrmwan.cocktailcatalogue.main.core.datasource.local.CocktailRoomDatabase
import com.bddrmwan.cocktailcatalogue.main.core.datasource.local.LocalDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.datasource.remote.RemoteDataSourceImpl
import com.bddrmwan.cocktailcatalogue.main.core.services.CocktailServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        service: CocktailServices
    ): RemoteDataSourceImpl {
        return RemoteDataSourceImpl(service)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        database: CocktailRoomDatabase
    ): LocalDataSourceImpl {
        return LocalDataSourceImpl(database)
    }

    @Singleton
    @Provides
    fun provideCocktailDatabase(
        @ApplicationContext mContext: Context
    ): CocktailRoomDatabase {
        return Room.databaseBuilder(
            mContext,
            CocktailRoomDatabase::class.java,
            "cocktail_db"
        )
            .build()
    }
}