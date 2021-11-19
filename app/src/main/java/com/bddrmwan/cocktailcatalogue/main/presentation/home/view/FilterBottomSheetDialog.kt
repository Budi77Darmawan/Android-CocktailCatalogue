package com.bddrmwan.cocktailcatalogue.main.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.databinding.DialogBottomSheetFilterBinding
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterCocktail
import com.bddrmwan.cocktailcatalogue.main.core.model.FilterEnum
import com.bddrmwan.cocktailcatalogue.main.extensions.gone
import com.bddrmwan.cocktailcatalogue.main.extensions.setBackStackData
import com.bddrmwan.cocktailcatalogue.main.extensions.toast
import com.bddrmwan.cocktailcatalogue.main.extensions.visible
import com.bddrmwan.cocktailcatalogue.main.presentation.home.adapter.CategoryFilterAdapter
import com.bddrmwan.cocktailcatalogue.main.presentation.home.viewmodel.FilterViewModel
import com.bddrmwan.cocktailcatalogue.main.utils.Const
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: DialogBottomSheetFilterBinding? = null
    private val binding get() = _binding!!

    private val filterViewModel: FilterViewModel by viewModels()
    private var defaultSelectedFilter: FilterCocktail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedBottomSheetDialogThemeWhite)
        arguments?.let {
            defaultSelectedFilter = FilterBottomSheetDialogArgs.fromBundle(it).selectedFilter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener { dlg ->
            val dialog = dlg as? BottomSheetDialog
            val bottomSheetInternal = dialog?.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheetInternal?.let {
                BottomSheetBehavior.from(it).state =
                    BottomSheetBehavior.STATE_EXPANDED
            }
        }
        _binding = DialogBottomSheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterViewModel.getCategoryFilter(FilterEnum.ALCOHOLIC)
        filterViewModel.getCategoryFilter(FilterEnum.GLASS)
        filterViewModel.getCategoryFilter(FilterEnum.CATEGORY)
        setupView()
        initListener()
        initSubscribeLiveData()
    }

    private fun setupView() {
        binding.apply {
            if (defaultSelectedFilter == null) {
                btnClose.visible()
                btnReset.gone()
            } else {
                btnClose.gone()
                btnReset.visible()
            }
        }
    }

    private fun initListener() {
        binding.apply {
            btnClose.setOnClickListener { findNavController().popBackStack() }
            btnApplyFilter.setOnClickListener {
                filterViewModel.selectedCategory?.let {
                    setBackStackData(
                        Const.SELECTED_CATEGORY_FILTER,
                        it
                    )
                } ?: run {
                    if (defaultSelectedFilter == null) toast(getString(R.string.no_filter_selected))
                    else findNavController().popBackStack()
                }
            }
            btnReset.setOnClickListener {
                setBackStackData(
                    Const.RESET_CATEGORY_FILTER,
                    true
                )
            }
        }
    }

    private fun initSubscribeLiveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filterViewModel.categoryFilter.collect {
                    setDataRecyclerView(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filterViewModel.onLoading.collect {
                    showLoading(it)
                }
            }
        }
    }

    private fun setDataRecyclerView(data: List<FilterCocktail>?) {
        binding.apply {
            data?.groupBy { it.filterBy }?.onEach { (filterEnum, list) ->
                val filterAdapter = CategoryFilterAdapter {
                    defaultSelectedFilter = null
                    filterViewModel.setSelectedCategory(it)
                }
                val layoutManagerAdapter = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
                layoutManagerAdapter.justifyContent = JustifyContent.FLEX_START
                filterAdapter.setData(list.take(6), defaultSelectedFilter)
                when (filterEnum) {
                    FilterEnum.ALCOHOLIC -> {
                        rvAlcoholic.adapter = filterAdapter
                        rvAlcoholic.layoutManager = layoutManagerAdapter
                    }
                    FilterEnum.GLASS -> {
                        rvGlass.adapter = filterAdapter
                        rvGlass.layoutManager = layoutManagerAdapter
                    }
                    FilterEnum.CATEGORY -> {
                        rvCategory.adapter = filterAdapter
                        rvCategory.layoutManager = layoutManagerAdapter
                    }
                }
            }
        }
    }

    private fun showLoading(visible: Boolean) {
        if (visible) {
            binding.viewCategoryFilter.gone()
            binding.progressCircular.visible()
        } else {
            binding.viewCategoryFilter.visible()
            binding.progressCircular.gone()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}