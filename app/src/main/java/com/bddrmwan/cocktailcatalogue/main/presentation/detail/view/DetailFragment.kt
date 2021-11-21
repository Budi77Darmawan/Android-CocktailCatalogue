package com.bddrmwan.cocktailcatalogue.main.presentation.detail.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bddrmwan.cocktailcatalogue.R
import com.bddrmwan.cocktailcatalogue.databinding.ContainerIngredientViewBinding
import com.bddrmwan.cocktailcatalogue.databinding.FragmentDetailBinding
import com.bddrmwan.cocktailcatalogue.main.core.model.Cocktail
import com.bddrmwan.cocktailcatalogue.main.extensions.*
import com.bddrmwan.cocktailcatalogue.main.presentation.detail.adapter.TagsAdapter
import com.bddrmwan.cocktailcatalogue.main.presentation.detail.viewmodel.DetailViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels()
    private var cocktail: Cocktail? = null
    private var isBookmark = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cocktail = DetailFragmentArgs.fromBundle(it).detailCocktail
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        cocktail?.instructions?.let {
//            setupView(cocktail)
//        } ?: run {
//            cocktail?.id?.let { id -> detailViewModel.getDetailCocktail(id) }
//        }

        cocktail?.id?.let { id -> detailViewModel.getDetailCocktail(id) }

        initListener()
        initSubscribeLiveData()
    }

    private fun initListener() {
        binding.apply {
            btnBookmark.setOnClickListener {
                if (isBookmark) {
                    cocktail?.let { it1 -> detailViewModel.deleteFromBookmark(it1) }
                    toast("DELETE BOOKMARK")
                } else {
                    cocktail?.let { it1 -> detailViewModel.addToBookmark(it1) }
                    toast("ADD BOOKMARK")
                }
                setBookmarkView(!isBookmark)
            }
        }
    }

    private fun setBookmarkView(isBookmarked: Boolean) {
        isBookmark = isBookmarked
        if (isBookmarked) {
            binding.btnBookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark
                )
            )
        } else {
            binding.btnBookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_outline
                )
            )
        }
    }

    private fun setupView(detail: Cocktail?) {
        binding.apply {
            toolbarLayout.title = detail?.name
            toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
            toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

            imgCocktail.loadImage(
                detail?.image,
                getProgressDrawable(requireContext())
            )
            tvInstruction.text = detail?.instructions

            detail?.ingredient?.let {
                binding.containerIngredient.removeAllViews()
                it.forEachIndexed { index, ingredient ->
                    val inflater = LayoutInflater.from(requireContext())
                    val inflaterBinding =
                        ContainerIngredientViewBinding.inflate(inflater, null, false)
                    binding.containerIngredient.addView(inflaterBinding.root)
                    inflaterBinding.tvName.text = ingredient.name
                    inflaterBinding.tvMeasure.text = ingredient.measure
                    if (index + 1 == detail.ingredient.size) inflaterBinding.dividerLine.gone()
                }
            }

            detail?.tags?.let {
                val adapterView = TagsAdapter()
                adapterView.setData(it)
                val layoutManagerAdapter = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
                layoutManagerAdapter.justifyContent = JustifyContent.FLEX_START
                binding.rvTags.apply {
                    adapter = adapterView
                    layoutManager = layoutManagerAdapter
                }
            } ?: run {
                binding.textViewTags.text = getString(R.string.no_tags)
            }
        }
    }

    private fun initSubscribeLiveData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.detailCocktail.collect {
                    cocktail = it
                    setupView(it)
                    setBookmarkView(it.isBookmark)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}