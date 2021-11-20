package com.bddrmwan.cocktailcatalogue.main.presentation.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private var _detailCocktail = MutableSharedFlow<Cocktail>()
    val detailCocktail get() = _detailCocktail.asSharedFlow()

    fun getDetailCocktail(id: String) {
        viewModelScope.launch {
            detailUseCase.getDetailCocktail(id)
                .onStart {  }
                .catch {  }
                .collect {
                    it?.let { cocktail ->
                        _detailCocktail.emit(cocktail)
                    }
                }
        }
    }

}