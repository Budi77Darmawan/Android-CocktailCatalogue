package com.bddrmwan.cocktailcatalogue.main.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.presentation.home.usecase.IHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: IHomeUseCase
) : ViewModel() {

    private var _listCocktail = MutableStateFlow<List<Cocktail>>(listOf())
    private var _listCocktailByName = MutableStateFlow<List<Cocktail>?>(null)
    private var _listCocktailByFilter = MutableStateFlow<List<Cocktail>?>(null)
    private var letter = 'a'
    private var onSearch = false
    private var onRequest = false
    private var _onLoading = MutableStateFlow(false)

    val listCocktail get() = _listCocktail
    val listCocktailByName get() = _listCocktailByName
    val listCocktailByFilter get() = _listCocktailByFilter
    val onLoading get() = _onLoading

    fun getCocktailByLetter() {
        if (!checkStateRequest(letter)) {
            onRequest = true
            viewModelScope.launch {
                homeUseCase.getCocktailByLetter(letter.toString())
                    .onStart {
                        stateLoading(true)
                    }
                    .catch {
                        onRequest = false
                        stateLoading(false)
                    }
                    .collect {
                        onRequest = false
                        stateLoading(false)
                        letter++
                        it?.let { listCocktail ->
                            _listCocktail.value += listCocktail
                        } ?: run {
                            getCocktailByLetter()
                        }
                    }
            }
        }
    }

    fun searchCocktailByName(name: String) {
        stateSearchBar(true)
        onSearch = true
        viewModelScope.launch {
            homeUseCase.getCocktailByName(name)
                .onStart {
                    stateLoading(true)
                }
                .catch {
                    stateLoading(false)
                }
                .collect {
                    stateLoading(false)
                    _listCocktailByName.value = it
                }
        }
    }

    fun searchCocktailByFilter(filter: FilterEnum, category: String) {
        stateSearchBar(false)
        viewModelScope.launch {
            homeUseCase.getCocktailByFilter(filter, category)
                .onStart {
                    stateLoading(true)
                }
                .catch {
                    stateLoading(false)
                }
                .collect {
                    stateLoading(false)
                    _listCocktailByFilter.value = it
                }
        }
    }

    fun stateSearchBar(onSearch: Boolean) {
        this.onSearch = onSearch
        if (!onSearch) _listCocktailByName.value = null
        else _listCocktailByFilter.value = null
    }

    private fun checkStateRequest(letter: Char): Boolean {
        return when {
            onSearch -> true
            onRequest -> true
            else -> letter == 'z'
        }
    }

    private fun stateLoading(visible: Boolean) {
        viewModelScope.launch {
            if (visible) {
                when {
                    onSearch -> _onLoading.value = visible
                    onRequest -> {
                        if (letter.compareTo('a') == 0) {
                            _onLoading.value = visible
                        }
                    }
                }
            } else _onLoading.value = visible
        }
    }
}