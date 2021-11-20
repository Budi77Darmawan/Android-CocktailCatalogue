package com.bddrmwan.cocktailcatalogue.main.presentation.home.usecase

import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.core.repository.IFilterCocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IFilterUseCase {
    fun getCategoryFilter(filter: FilterEnum): Flow<List<FilterCocktail>?>
}

class FilterUseCaseImpl @Inject constructor(
    private val filterCocktailRepo: IFilterCocktailRepository
) : IFilterUseCase {
    override fun getCategoryFilter(filter: FilterEnum): Flow<List<FilterCocktail>?> {
        return when (filter) {
            FilterEnum.ALCOHOLIC -> filterCocktailRepo.getCategoryFilterCocktailByAlcoholic()
            FilterEnum.GLASS -> filterCocktailRepo.getCategoryFilterCocktailByGlass()
            FilterEnum.CATEGORY -> filterCocktailRepo.getCategoryFilterCocktailByCategory()
        }
    }
}