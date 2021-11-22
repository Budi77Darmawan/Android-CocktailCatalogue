package com.bddrmwan.cocktailcatalogue.main.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.databinding.FragmentHomeBinding
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.extensions.gone
import com.bddrmwan.cocktailcatalogue.main.extensions.hideKeyboard
import com.bddrmwan.cocktailcatalogue.main.extensions.visible
import com.bddrmwan.cocktailcatalogue.main.presentation.home.adapter.CocktailAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseGridFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    protected val binding get() = _binding!!

    private var onDebouncing: Job? = null
    protected var cocktailAdapter: CocktailAdapter? = null

    abstract fun actionSearch(query: String)
    abstract fun searchQueryChanged(query: String)
    abstract fun actionCancelSearch()
    abstract fun searchQueryIsEmpty()
    abstract fun actionClickedFilter()
    abstract fun navigateToDetail(cocktail: Cocktail?)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchBarView()
        initCocktailRecyclerView()
    }

    private fun initSearchBarView() {
        binding.searchBar.apply {
            inputSearch.doAfterTextChanged {
                onDebouncing?.cancel()
                if (it?.isEmpty() == true) {
                    showErrorMessage(visible = false)
                    searchQueryIsEmpty()
                    iconCancelSearch.gone()
                } else {
                    onDebouncing = lifecycleScope.launch {
                        delay(500L)
                        searchQueryChanged(inputSearch.text.toString().trim())
                        onDebouncing?.cancel()
                    }
                    iconCancelSearch.visible()
                }
            }
            inputSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    inputSearch.clearFocus()
                    actionSearch(inputSearch.text.toString().trim())
                    hideKeyboard(this.inputSearch)
                    true
                } else false
            }
            iconCancelSearch.setOnClickListener {
                binding.rvCocktail.adapter = cocktailAdapter
                inputSearch.text?.clear()
                inputSearch.clearFocus()
                iconCancelSearch.gone()
                showErrorMessage(visible = false)
                actionCancelSearch()
                hideKeyboard(it)
            }
            iconFilter.setOnClickListener {
                actionClickedFilter()
            }
        }
    }

    private fun setCocktailAdapter() {
        cocktailAdapter = CocktailAdapter {
            navigateToDetail(it)
        }
        binding.rvCocktail.adapter = cocktailAdapter
    }

    private fun initCocktailRecyclerView() {
        setCocktailAdapter()
        val layoutManagerCocktail =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvCocktail.apply {
            layoutManager = layoutManagerCocktail
        }
    }

    protected fun showErrorMessage(
        msg: String? = getString(R.string.internal_server_error),
        visible: Boolean = true
    ) {
        binding.tvErrorMessage.apply {
            text = msg
            if (visible) visible()
            else gone()
        }
    }

    protected fun showLoading(visible: Boolean) {
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
        onDebouncing?.cancel()
        _binding = null
    }
}