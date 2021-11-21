package com.bddrmwan.cocktailcatalogue.main.presentation.detail.usecase

import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.repository.IBookmarkRepository
import com.bddrmwan.cocktailcatalogue.main.core.repository.ICocktailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface IDetailUseCase {
    fun getDetailCocktail(id: String): Flow<Cocktail?>
    fun addToBookmark(cocktail: Cocktail)
    fun deleteFromBookmark(cocktail: Cocktail)
}

class DetailUseCaseImpl @Inject constructor(
    private val cocktailRepo: ICocktailRepository,
    private val bookmarkRepo: IBookmarkRepository
) : IDetailUseCase {
    override fun getDetailCocktail(id: String): Flow<Cocktail?> {
        return flow {
            val res = bookmarkRepo.getDetailCocktail(id)
            res.collect {
                it?.let {
                    emit(it)
                } ?: run {
                    val remote = cocktailRepo.getDetailCocktail(id)
                    remote.collect { cocktailRemote ->
                        emit(cocktailRemote)
                    }
                }
            }
        }
    }

    override fun addToBookmark(cocktail: Cocktail) =
        bookmarkRepo.addToBookmark(cocktail)

    override fun deleteFromBookmark(cocktail: Cocktail) =
        bookmarkRepo.deleteFromBookmark(cocktail)

}