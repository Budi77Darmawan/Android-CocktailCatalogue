package com.bddrmwan.cocktailcatalogue.main.presentation.home.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.extensions.gone
import com.bddrmwan.cocktailcatalogue.main.extensions.visible
import com.bddrmwan.cocktailcatalogue.main.presentation.home.adapter.CocktailAdapter
import com.bddrmwan.cocktailcatalogue.main.presentation.home.viewmodel.HomeViewModel
import com.bddrmwan.cocktailcatalogue.main.utils.Const
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseGridFragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var searchAdapter: CocktailAdapter? = null
    private var selectedFilter: FilterCocktail? = null

    enum class StateAction {
        DEFAULT, SEARCH, FILTER
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCocktailByLetter()
        initSetResultFragment()
        initSubscribeLiveData()
        initInfiniteScroll()
    }

    private fun initInfiniteScroll() {
        binding.rvCocktail.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val query = binding.searchBar.inputSearch.text.toString().trim()
                if (query.isNotEmpty() || selectedFilter != null) return

                if (dy > 0) {
                    val layoutMgr = recyclerView.layoutManager as? GridLayoutManager
                    val itemCount = layoutMgr?.itemCount
                    val lastVisible = layoutMgr?.findLastCompletelyVisibleItemPosition() ?: 0
                    val isLastVisible = lastVisible >= itemCount?.minus(6) ?: 1
                    if (isLastVisible) {
                        getCocktailByLetter()
                    }
                }
            }
        })
    }


    private fun setSearchCocktailAdapter() {
        searchAdapter = CocktailAdapter {
            navigateToDetail(it)
        }
        binding.rvCocktail.adapter = searchAdapter
    }

    private fun initSetResultFragment() {
        setFragmentResultListener(Const.REQ_SELECTED_CATEGORY_FILTER) { _, bdl ->
            selectedFilter = bdl.getParcelable(Const.SELECTED_CATEGORY_FILTER)
            selectedFilter?.let {
                binding.searchBar.inputSearch.text?.clear()
                binding.searchBar.iconCancelSearch.gone()
                it.name?.let { name ->
                    homeViewModel.searchCocktailByFilter(it.filterBy, name)
                }
            }
        }

        setFragmentResultListener(Const.REQ_RESET_CATEGORY_FILTER) { _, bdl ->
            val resetFilter = bdl.getBoolean(Const.RESET_CATEGORY_FILTER)
            if (resetFilter) {
                selectedFilter = null
                binding.rvCocktail.adapter = cocktailAdapter
            }
        }
    }

    private fun initSubscribeLiveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.listCocktail.collect {
                    if (it.isNotEmpty()) {
                        cocktailAdapter?.setData(it)
                        showErrorMessage(visible = false)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.listCocktailByName.collect {
                    val query = binding.searchBar.inputSearch.text.toString().trim()
                    if (query.isNotEmpty()) {
                        setSearchCocktailAdapter()
                        if (it.isNullOrEmpty()) {
                            searchAdapter?.setData(listOf(), selectedFilter)
                            val message = "\"${query}\" not found!"
                            showErrorMessage(message)
                        } else {
                            searchAdapter?.setData(it, selectedFilter)
                            showErrorMessage(visible = false)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.listCocktailByFilter.collect {
                    if (selectedFilter != null) {
                        setSearchCocktailAdapter()
                        if (it.isNullOrEmpty()) {
                            searchAdapter?.setData(listOf(), selectedFilter)
                            val message = "\"${selectedFilter?.name}\" not found!"
                            showErrorMessage(message)
                        } else {
                            searchAdapter?.setData(it, selectedFilter)
                            showErrorMessage(visible = false)
                        }
                        showLoading(false)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.onLoading.collect {
                    showLoading(it)
                }
            }
        }
    }

    private fun getCocktailByLetter() {
        homeViewModel.getCocktailByLetter()
    }

    override fun actionSearch(query: String) {
        selectedFilter = null
        homeViewModel.stateActionShow(StateAction.SEARCH)
        homeViewModel.searchCocktailByName(query)
    }

    override fun actionCancelSearch() {
        homeViewModel.stateActionShow(StateAction.DEFAULT)
    }

    override fun searchQueryIsEmpty() {
        if (selectedFilter == null) {
            homeViewModel.stateActionShow(StateAction.DEFAULT)
            binding.rvCocktail.adapter = cocktailAdapter
        }
    }

    override fun searchQueryChanged(query: String) {
    }

    override fun actionClickedFilter() {
        val navigateToFilter =
            HomeFragmentDirections.actionHomeFragmentToFilterBottomSheetDialog()
        navigateToFilter.selectedFilter = selectedFilter
        findNavController().navigate(navigateToFilter)
    }

    override fun navigateToDetail(cocktail: Cocktail?) {
        val toDetail = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
        toDetail.detailCocktail = cocktail
        findNavController().navigate(toDetail)
    }
}