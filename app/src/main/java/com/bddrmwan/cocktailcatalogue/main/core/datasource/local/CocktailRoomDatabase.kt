package com.bddrmwan.cocktailcatalogue.main.core.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail

@Database(entities = [Cocktail::class], version = 1)
@TypeConverters(ConvertersDao::class)
abstract class CocktailRoomDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDao
}