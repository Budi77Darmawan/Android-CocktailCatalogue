package com.bddrmwan.cocktailcatalogue.main.core.datasource

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T? = null) : Resource<T>()
    data class Error<T>(val message: String?, val data: T? = null) : Resource<T>()
}