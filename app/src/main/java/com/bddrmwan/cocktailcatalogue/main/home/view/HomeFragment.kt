package com.bddrmwan.cocktailcatalogue.main.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bddrmwan.cocktailcatalogue.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import com.bddrmwan.cocktailcatalogue.main.home.adapter.CocktailAdapter

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListCocktailView()
    }

    private fun setupListCocktailView() {
        val adapterCocktail = CocktailAdapter()
        val layoutManagerCocktail = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvCocktail.apply {
            adapter = adapterCocktail
            layoutManager = layoutManagerCocktail
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}