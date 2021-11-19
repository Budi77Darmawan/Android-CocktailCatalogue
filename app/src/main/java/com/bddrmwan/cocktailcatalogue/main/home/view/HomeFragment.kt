package com.bddrmwan.cocktailcatalogue.main.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.databinding.FragmentHomeBinding
import com.bddrmwan.cocktailcatalogue.main.extension.gone
import com.bddrmwan.cocktailcatalogue.main.extension.hideKeyboard
import com.bddrmwan.cocktailcatalogue.main.extension.toast
import com.bddrmwan.cocktailcatalogue.main.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import com.bddrmwan.cocktailcatalogue.main.home.adapter.CocktailAdapter
import com.bddrmwan.cocktailcatalogue.main.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private var adapterCocktail: CocktailAdapter? = null

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
        initSearchBarView()
        initCocktailRecyclerView()
        initSubscribeLiveData()
    }

    private fun initSubscribeLiveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.listCocktail.collect {
                    adapterCocktail?.addData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.listCocktailByName.collect {
                    if (it.isNullOrEmpty()) {
                        val query = binding.searchBar.inputSearch.text.toString().trim()
                        if (query.isNotEmpty()) toast("\"${query}\" not found!")
                    }
                    else adapterCocktail?.setData(it)
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
                    homeViewModel.clearSearchBar()
                    iconCancelSearch.gone()
                }
                else iconCancelSearch.visible()
            }
            inputSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    homeViewModel.searchCocktailByName(inputSearch.text.toString().trim())
                    inputSearch.clearFocus()
                    hideKeyboard(this.inputSearch)
                    true
                } else false
            }
            iconCancelSearch.setOnClickListener {
                homeViewModel.clearSearchBar()
                inputSearch.text?.clear()
                inputSearch.clearFocus()
                iconCancelSearch.gone()
                hideKeyboard(it)
            }
            iconFilter.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_homeFragment_to_filterBottomSheetDialog)
            }
        }
    }

    private fun initCocktailRecyclerView() {
        adapterCocktail = CocktailAdapter {
            toast(it?.name)
        }
        val layoutManagerCocktail =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvCocktail.apply {
            adapter = adapterCocktail
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
        }
        else {
            binding.rvCocktail.visible()
            binding.progressCircular.gone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}