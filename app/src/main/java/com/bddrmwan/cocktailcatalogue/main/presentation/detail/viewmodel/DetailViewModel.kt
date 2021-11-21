package com.bddrmwan.cocktailcatalogue.main.presentation.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bddrmwan.cocktailcatalogue.main.core.datasource.Resource
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.presentation.detail.usecase.IDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailUseCase: IDetailUseCase
): ViewModel() {

    private var _detailCocktail = MutableSharedFlow<Resource<Cocktail>>()
    val detailCocktail get() = _detailCocktail.asSharedFlow()

    fun getDetailCocktail(id: String) {
        viewModelScope.launch {
            detailUseCase.getDetailCocktail(id)
                .onStart {
                    _detailCocktail.emit(Resource.Loading())
                }
                .catch {
                    val msg = it.localizedMessage ?: "Internal server error"
                    _detailCocktail.emit(Resource.Error(msg))
                }
                .collect {
                    it?.let { cocktail ->
                        _detailCocktail.emit(Resource.Success(cocktail))
                    }
                }
        }
    }

    fun addToBookmark(cocktail: Cocktail) {
        viewModelScope.launch {
            detailUseCase.addToBookmark(cocktail)
        }
    }


    fun deleteFromBookmark(cocktail: Cocktail) {
        viewModelScope.launch {
            detailUseCase.deleteFromBookmark(cocktail)
        }
    }

}