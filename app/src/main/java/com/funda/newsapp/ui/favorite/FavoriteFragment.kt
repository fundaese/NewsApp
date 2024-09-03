package com.funda.newsapp.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.funda.newsapp.R
import com.funda.newsapp.common.gone
import com.funda.newsapp.common.viewBinding
import com.funda.newsapp.common.visible
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.databinding.FragmentFavoriteBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite), FavoriteAdapter.ArticleListener {

    private val binding by viewBinding(FragmentFavoriteBinding::bind)

    private val viewModel by viewModels<FavoriteViewModel>()

    private var bottomNavigationView: BottomNavigationView? = null

    private val favAdapter by lazy { FavoriteAdapter(this) }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.visible()

        setHasOptionsMenu(true)

        viewModel.getFavArticles()

        setupToolbar()
        setupRecyclerView()
        setupBackPressedCallback()

        observeData()
    }

    //Toolbar for favorite fragment
    private fun setupToolbar() {
        binding.toolbar.setTitle(resources.getString(R.string.favorite_news))
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_profile -> {
                    findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    //Bind recyclerview
    private fun setupRecyclerView() {
        binding.rvFavorites.adapter = favAdapter
    }

    //Back pressed control
    private fun setupBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

    //Observe Data from ViewModel
    private fun observeData() = with(binding) {
        viewModel.favState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavState.Loading -> {
                    progressBar.visible()
                }
                is FavState.Data -> {
                    favAdapter.submitList(state.articles)
                    progressBar.gone()
                }
                is FavState.Error -> {
                    progressBar.gone()
                }
            }
        }
    }

    override fun onNewsClick(id: Int) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToNewsDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onDeleteClick(article: ArticleUI) {
        viewModel.deleteArticleFromFav(article)
    }
}
