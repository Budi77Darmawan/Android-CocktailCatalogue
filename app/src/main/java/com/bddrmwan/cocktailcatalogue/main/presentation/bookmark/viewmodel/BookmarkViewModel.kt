package com.bddrmwan.cocktailcatalogue.main.presentation.bookmark.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.presentation.bookmark.usecase.IBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkUseCase: IBookmarkUseCase
): ViewModel() {

    private var _listCocktailBookmarked = MutableStateFlow<List<Cocktail>?>(null)
    val listCocktailBookmarked get() = _listCocktailBookmarked


    fun getAllCocktailBookmarked() {
        viewModelScope.launch {
            bookmarkUseCase.getAllCocktails()
                .onStart {  }
                .catch {  }
                .collect {
                    _listCocktailBookmarked.value = it
                }
        }
    }

    fun searchCocktailsByName(name: String) {
        viewModelScope.launch {
            bookmarkUseCase.searchCocktailsByName(name)
                .onStart {  }
                .catch {
                    Log.d("BOOKMARK-ERROR", it.localizedMessage ?: "")
                }
                .collect {
                    _listCocktailBookmarked.value = it
                }
        }
    }
}