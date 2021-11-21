package com.bddrmwan.cocktailcatalogue.main.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.presentation.home.usecase.IFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterUseCase: IFilterUseCase
) : ViewModel() {

    private var _categoryFilter = MutableSharedFlow<List<FilterCocktail>?>()
    private var _tempCategoryFilter = mutableListOf<FilterCocktail>()
    private var _selectedCategory: FilterCocktail? = null
    private var _onLoading = MutableSharedFlow<Boolean>()
    private var numOfLoading = 0

    val categoryFilter get() = _categoryFilter.asSharedFlow()
    val selectedCategory get() = _selectedCategory
    val onLoading get() = _onLoading.asSharedFlow()

    fun getCategoryFilter(filter: FilterEnum) {
        viewModelScope.launch {
            filterUseCase.getCategoryFilter(filter)
                .onStart {
                    stateLoading(true)
                }
                .catch {
                    stateLoading(false)
                }
                .collect {
                    stateLoading(false)
                    _categoryFilter.emit(it)
                    it?.let { res -> _tempCategoryFilter.addAll(res) }
                    _tempCategoryFilter.distinctBy { filter -> filter.name }
                }
        }
    }

    fun setSelectedCategory(filter: FilterCocktail?) {
        viewModelScope.launch {
            filter?.let { filter ->
                _tempCategoryFilter.onEach { temp ->
                    if (filter.name == temp.name) {
                        temp.isSelected = true
                        _selectedCategory = temp
                    } else temp.isSelected = false
                }
                _categoryFilter.emit(_tempCategoryFilter)
            }
        }
    }

    private fun stateLoading(visible: Boolean) {
        if (visible) numOfLoading++
        else numOfLoading--
        viewModelScope.launch {
            if (numOfLoading <= 0) _onLoading.emit(false)
            else _onLoading.emit(true)
        }
    }
}