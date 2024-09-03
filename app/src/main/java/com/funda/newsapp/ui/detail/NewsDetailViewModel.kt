package com.funda.newsapp.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funda.newsapp.common.Resource
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.data.repository.ArticleRepository
import com.funda.newsapp.di.IoDispatcher
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    application: Application,
): ViewModel() {

    private var _newsDetailState = MutableLiveData<NewsDetailState>()
    val newsDetailState: LiveData<NewsDetailState>
        get() = _newsDetailState

    private val auth = FirebaseAuth.getInstance()
    private val userId: String
        get() = auth.currentUser?.uid.orEmpty()

    fun getNewsDetail(id: Int) {
        viewModelScope.launch {
            _newsDetailState.value = NewsDetailState.Loading
            val result = articleRepository.getArticleDetail(id)

            when (result) {
                is Resource.Success<*> -> {
                    _newsDetailState.value = NewsDetailState.Data(result.data as ArticleUI)
                }

                is Resource.Error -> {
                    _newsDetailState.value = NewsDetailState.Error(result.throwable)
                }
            }
        }
    }

    fun addArticleToFav(articleUI: ArticleUI) {
        viewModelScope.launch {
            articleRepository.addArticlesToFav(articleUI, userId)
        }
    }
}

sealed interface NewsDetailState {
    object Loading: NewsDetailState
    data class Data(val newsResponse: ArticleUI): NewsDetailState
    data class Error(val throwable: Throwable): NewsDetailState
}