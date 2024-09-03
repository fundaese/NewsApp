package com.funda.newsapp.ui.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funda.newsapp.common.Resource
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val articleRepository: ArticleRepository, application: Application
): ViewModel() {

    private var _newsState = MutableLiveData<NewsState>()
    val newsState: LiveData<NewsState>
        get() = _newsState

    fun getArticles(query: String? = null) {
        _newsState.value = NewsState.Loading
        viewModelScope.launch {
            try {
                val result = articleRepository.getArticles(search = query)
                if (result is Resource.Success) {
                    _newsState.value = NewsState.Data(result.data)
                } else if (result is Resource.Error) {
                    _newsState.value = NewsState.Error(result.throwable)
                }
            } catch (e: Exception) {
                _newsState.value = NewsState.Error(e)
            }
        }
    }
}

sealed interface NewsState {
    object Loading : NewsState
    data class Data(val articles: List<ArticleUI>) : NewsState
    data class Error(val throwable: Throwable) : NewsState
}