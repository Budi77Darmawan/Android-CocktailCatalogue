package com.bddrmwan.cocktailcatalogue.main.presentation.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.databinding.FragmentHomeBinding
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.extensions.*
import com.bddrmwan.cocktailcatalogue.main.presentation.home.adapter.CocktailAdapter

abstract class BaseGridFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    protected val binding get() = _binding!!

    protected var cocktailAdapter: CocktailAdapter? = null

    abstract fun actionSearch(query: String)
    abstract fun actionCancelSearch()
    abstract fun querySearchIsEmpty()
    abstract fun actionClickedFilter()

    protected fun navigateToDetail(cocktail: Cocktail?) {
        val toDetail = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
        toDetail.detailCocktail = cocktail
        findNavController().navigate(toDetail)
    }

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
                if (it?.isEmpty() == true) {
                    querySearchIsEmpty()
                    iconCancelSearch.gone()
                } else iconCancelSearch.visible()
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
                inputSearch.text?.clear()
                inputSearch.clearFocus()
                iconCancelSearch.gone()
                actionCancelSearch()
                binding.rvCocktail.adapter = cocktailAdapter
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

    protected fun showErrorMessage(msg: String? = getString(R.string.internal_server_error), visible: Boolean = true) {
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
        _binding = null
    }
}