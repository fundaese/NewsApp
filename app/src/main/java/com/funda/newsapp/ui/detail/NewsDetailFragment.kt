package com.funda.newsapp.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.funda.newsapp.common.visible
import com.funda.newsapp.common.gone
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.funda.newsapp.R
import com.funda.newsapp.common.loadImage
import com.funda.newsapp.common.viewBinding
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.databinding.FragmentNewsDetailBinding
import com.funda.newsapp.ui.favorite.FavoriteFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private val binding by viewBinding(FragmentNewsDetailBinding::bind)

    private val args by navArgs<NewsDetailFragmentArgs>()

    private val viewModel by viewModels<NewsDetailViewModel>()

    private var bottomNavigationView: BottomNavigationView? = null

    var news: ArticleUI ?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Navigation Visibility
        bottomNavigationView = getActivity()?.findViewById(R.id.bottomNavigationView);
        bottomNavigationView?.gone();

        viewModel.getNewsDetail(args.id)

        setupBackPressedCallback()
        setupToolbar()
        observeData()
    }

    private fun observeData()  = with(binding) {

        viewModel.newsDetailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                NewsDetailState.Loading -> {
                  detailProgressBar.visible()
                }

                is NewsDetailState.Data -> {
                    detailProgressBar.gone()
                    news = state.newsResponse
                    if (state.newsResponse != null) {
                        tvTitle.text = state.newsResponse.title
                        tvSummary.text = state.newsResponse.summary
                        tvDate.text = state.newsResponse.publishedAt
                        imgNews.loadImage(state.newsResponse.imageUrl)
                    }
                }

                is NewsDetailState.Error -> {
                    detailProgressBar.gone()
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }

    //Share News
    private fun shareNews() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${news?.title}\n\n${news?.summary}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_article)))
    }

    //Toolbar for detail fragment
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_share -> {
                    shareNews()
                    true
                }
                R.id.action_favorite -> {
                    news?.let { viewModel.addArticleToFav(it) }
                    menuItem.setIcon(R.drawable.ic_fav)
                    Snackbar.make(requireView(), "Article added to your favorites list", Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    //Back pressed control
    private fun setupBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }
}