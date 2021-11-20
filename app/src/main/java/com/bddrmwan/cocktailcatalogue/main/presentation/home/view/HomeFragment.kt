package com.bddrmwan.cocktailcatalogue.main.presentation.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bddrmwan.cocktailcatalogue.databinding.FragmentHomeBinding
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import com.bddrmwan.cocktailcatalogue.main.presentation.home.adapter.CocktailAdapter
import com.bddrmwan.cocktailcatalogue.main.presentation.home.viewmodel.HomeViewModel
import com.bddrmwan.cocktailcatalogue.main.utils.Const
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private var cocktailAdapter: CocktailAdapter? = null
    private var searchAdapter: CocktailAdapter? = null
    private var selectedFilter: FilterCocktail? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCocktailByLetter()
        initSetResultFragment()
        initSearchBarView()
        initCocktailRecyclerView()
        initSubscribeLiveData()
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
                    cocktailAdapter?.setData(it)
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
                            toast("\"${query}\" not found!")
                        } else {
                            searchAdapter?.setData(it, selectedFilter)
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
                            toast("\"${selectedFilter?.name}\" not found!")
                        } else {
                            searchAdapter?.setData(it, selectedFilter)
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

    private fun initSearchBarView() {
        binding.searchBar.apply {
            inputSearch.doAfterTextChanged {
                if (it?.isEmpty() == true) {
                    if (selectedFilter == null) {
                        homeViewModel.stateSearchBar(false)
                        binding.rvCocktail.adapter = cocktailAdapter
                    }
                    iconCancelSearch.gone()
                } else iconCancelSearch.visible()
            }
            inputSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    selectedFilter = null
                    inputSearch.clearFocus()
                    homeViewModel.searchCocktailByName(inputSearch.text.toString().trim())
                    hideKeyboard(this.inputSearch)
                    true
                } else false
            }
            iconCancelSearch.setOnClickListener {
                homeViewModel.stateSearchBar(false)
                binding.rvCocktail.adapter = cocktailAdapter
                iconCancelSearch.gone()
                inputSearch.text?.clear()
                inputSearch.clearFocus()
                hideKeyboard(it)
            }
            iconFilter.setOnClickListener {
                val navigateToFilter =
                    HomeFragmentDirections.actionHomeFragmentToFilterBottomSheetDialog()
                navigateToFilter.selectedFilter = selectedFilter
                findNavController().navigate(navigateToFilter)
            }
        }
    }

    private fun navigateToDetail(cocktail: Cocktail?) {
        val toDetail = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
        toDetail.detailCocktail = cocktail
        findNavController().navigate(toDetail)
    }

    private fun setCocktailAdapter() {
        cocktailAdapter = CocktailAdapter {
            navigateToDetail(it)
        }
        binding.rvCocktail.adapter = cocktailAdapter
    }


    private fun setSearchCocktailAdapter() {
        searchAdapter = CocktailAdapter {
            navigateToDetail(it)
        }
        binding.rvCocktail.adapter = searchAdapter
    }

    private fun initCocktailRecyclerView() {
        setCocktailAdapter()
        val layoutManagerCocktail =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvCocktail.apply {
            layoutManager = layoutManagerCocktail

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
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
    }

    private fun getCocktailByLetter() {
        homeViewModel.getCocktailByLetter()
    }

    private fun showLoading(visible: Boolean) {
        if (visible) {
            binding.rvCocktail.gone()
            binding.progressCircular.visible()
        } else {
            binding.rvCocktail.visible()
            binding.progressCircular.gone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}