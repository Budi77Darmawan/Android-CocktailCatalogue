package com.bddrmwan.cocktailcatalogue.main.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.home.usecase.IFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterUseCase: IFilterUseCase
) : ViewModel() {

    private var _categoryFilter = MutableSharedFlow<MutableMap<FilterEnum, List<FilterCocktail>?>>()
    private var _tempCategoryFilter = mutableMapOf<FilterEnum, List<FilterCocktail>?>()
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
                    _tempCategoryFilter[filter] = it
                    _categoryFilter.emit(_tempCategoryFilter)
                }
        }
    }

    fun setSelectedCategory(filter: FilterCocktail?) {
        viewModelScope.launch {
            filter?.let {
                _tempCategoryFilter.onEach { (_, list) ->
                    list?.onEach {
                        it.isSelected = if (filter.name == it.name) {
                            _selectedCategory = it
                            true
                        } else false

                    }
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