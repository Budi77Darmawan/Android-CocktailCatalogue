package com.bddrmwan.cocktailcatalogue.main.presentation.bookmark.view

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.extensions.gone
import com.bddrmwan.cocktailcatalogue.main.presentation.bookmark.viewmodel.BookmarkViewModel
import com.bddrmwan.cocktailcatalogue.main.presentation.home.view.BaseGridFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : BaseGridFragment() {

    private val bookmarkViewModel: BookmarkViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSubscribeLiveData()
        hideIconFilter()
    }

    override fun onResume() {
        super.onResume()
        val query = binding.searchBar.inputSearch.text.toString().trim()
        if (query.isEmpty()) bookmarkViewModel.getAllCocktailBookmarked()
    }

    private fun hideIconFilter() {
        binding.apply {
            searchBar.iconFilter.gone()
            (searchBar.viewSearch.layoutParams as ConstraintLayout.LayoutParams).apply {
                rightMargin = 0
            }
        }
    }

    private fun initSubscribeLiveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookmarkViewModel.listCocktailBookmarked.collect {
                    if (it.isNullOrEmpty()) {
                        val query = binding.searchBar.inputSearch.text.toString().trim()
                        if (query.isNotEmpty()) {
                            val message = "\"${query}\" not found!"
                            showErrorMessage(message)
                        } else {
                            showErrorMessage(getString(R.string.bokkmar_is_empty))
                        }
                        cocktailAdapter?.setData(it ?: listOf())
                    } else {
                        showErrorMessage(visible = false)
                        cocktailAdapter?.setData(it)
                    }
                }
            }
        }
    }

    override fun searchQueryChanged(query: String) {
        bookmarkViewModel.searchCocktailsByName(query)
    }

    override fun actionCancelSearch() {
        bookmarkViewModel.getAllCocktailBookmarked()
    }

    override fun searchQueryIsEmpty() {
        bookmarkViewModel.getAllCocktailBookmarked()
    }

    override fun actionSearch(query: String) {
    }

    override fun actionClickedFilter() {
    }

    override fun navigateToDetail(cocktail: Cocktail?) {
        val toDetail = BookmarkFragmentDirections.actionBookmarkFragmentToDetailFragment()
        toDetail.detailCocktail = cocktail
        findNavController().navigate(toDetail)
    }
}