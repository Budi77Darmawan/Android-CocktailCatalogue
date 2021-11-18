package com.bddrmwan.cocktailcatalogue.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.home.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private var _listCocktail = MutableSharedFlow<List<Cocktail>>()
    private var _listCocktailByName = MutableSharedFlow<List<Cocktail>?>()
    private var letter: Char = 'a'
    private var onSearch = false
    private var onRequest = false
    private var _onLoading = MutableSharedFlow<Boolean>()

    val listCocktail get() = _listCocktail.asSharedFlow()
    val listCocktailByName get() = _listCocktailByName.asSharedFlow()
    val onLoading get() = _onLoading.asSharedFlow()

    fun getCocktailByLetter() {
        if (!checkStateRequest(letter)) {
            onRequest = true
            viewModelScope.launch {
                homeUseCase.getCocktailByLetter(letter.toString())
                    .onStart {
                        stateLoading(true)
                        Log.d("REQUEST", "- START")
                    }
                    .catch {
                        onRequest = false
                        stateLoading(false)
                        Log.e("REQUEST", "- ERROR ${it.localizedMessage}")
                    }
                    .collect {
                        Log.d("REQUEST", "- SUCCESS $it")
                        onRequest = false
                        stateLoading(false)
                        letter++
                        it?.let { listCocktail ->
                            _listCocktail.emit(listCocktail)
                        } ?: run {
                            getCocktailByLetter()
                        }
                    }
            }
        }
    }

    fun searchCocktailByName(name: String) {
        onSearch = true
        viewModelScope.launch {
            homeUseCase.getCocktailByName(name)
                .onStart {
                    stateLoading(true)
                    Log.d("REQUEST", "- START")
                }
                .catch {
                    stateLoading(false)
                    Log.e("REQUEST", "- ERROR ${it.localizedMessage}")
                }
                .collect {
                    stateLoading(false)
                    Log.d("REQUEST", "- SUCCESS $it")
                    _listCocktailByName.emit(it)
                }
        }
    }

    fun clearSearchBar() {
        onSearch = false
        letter = 'a'
        viewModelScope.launch {
            _listCocktailByName.emit(null)
            getCocktailByLetter()
        }
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
                    onSearch -> _onLoading.emit(visible)
                    onRequest -> {
                        if (letter == 'a') _onLoading.emit(visible)
                    }
                }
            } else _onLoading.emit(visible)
        }
    }
}