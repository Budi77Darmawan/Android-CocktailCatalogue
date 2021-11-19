package com.bddrmwan.cocktailcatalogue.main.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterCocktail(
    val name: String?,
    var filterBy: FilterEnum,
    var isSelected: Boolean = false
) : Parcelable