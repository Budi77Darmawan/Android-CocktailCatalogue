package com.bddrmwan.cocktailcatalogue.main.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.databinding.DialogBottomSheetFilterBinding
import com.bddrmwan.cocktailcatalogue.main.extension.gone
import com.bddrmwan.cocktailcatalogue.main.extension.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: DialogBottomSheetFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedBottomSheetDialogThemeWhite)
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

    }

    private fun showLoading(visible: Boolean) {
        if (visible) {
            binding.viewCategoryFilter.gone()
            binding.progressCircular.visible()
        }
        else {
            binding.viewCategoryFilter.visible()
            binding.progressCircular.gone()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}