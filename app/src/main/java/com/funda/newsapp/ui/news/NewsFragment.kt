package com.funda.newsapp.ui.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.funda.newsapp.R
import com.funda.newsapp.common.gone
import com.funda.newsapp.common.viewBinding
import com.funda.newsapp.common.visible
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.databinding.FragmentNewsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news), NewsAdapter.ArticleListener{

    private val binding by viewBinding(FragmentNewsBinding::bind)

    private var bottomNavigationView: BottomNavigationView? = null
    private val newsAdapter by lazy { NewsAdapter(this) }
    private val viewModel by viewModels<NewsViewModel>()

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.visible()

        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        setupBackPressedCallback()

        viewModel.getArticles()

        observeData()
    }

    //Setup Toolbar
    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
            Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_profile -> {
                        findNavController().navigate(NewsFragmentDirections.actionNewsFragmentToProfileFragment())
                        return true
                    }
                }
                return false
            }
        })
    }

    //Setup RecyclerView
    private fun setupRecyclerView() {
        binding.recyclerView.adapter = newsAdapter
    }

    //Setup SearchView
    @SuppressLint("RestrictedApi")
    private fun setupSearchView() {
        val searchItem = binding.toolbar.menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as? SearchView

        searchView?.apply {
            queryHint = getString(R.string.search)
            val searchTextView = findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
            searchTextView.setHintTextColor(resources.getColor(android.R.color.darker_gray))
            searchTextView.setTextColor(resources.getColor(android.R.color.black))
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { query ->
                    if (query.length >= 3) {
                        viewModel.getArticles(query)
                    } else {
                        Snackbar.make(requireView(), "Enter at least 3 characters for the article you want to search", 1000).show()
                    }
                }
                return true
            }
        })
    }

    //Back Press Control
    private fun setupBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (findNavController().currentDestination?.id == R.id.newsFragment) {
                    activity?.finish()
                } else {
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun observeData()  = with(binding) {
        viewModel.newsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                NewsState.Loading -> {
                    progressBar.visible()
                }
                is NewsState.Data -> {
                    progressBar.gone()
                    newsAdapter.submitList(state.articles)
                }
                is NewsState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }

    override fun onNewsClick(id: Int) {
        val action = NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(id)
        findNavController().navigate(action)
    }
}
