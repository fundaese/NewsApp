package com.funda.newsapp.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funda.newsapp.common.Resource
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.data.repository.ArticleRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val articleRepository: ArticleRepository, application: Application
): ViewModel() {

    private var _favState = MutableLiveData<FavState>()
    val favState: LiveData<FavState>
        get() = _favState

    private val auth = FirebaseAuth.getInstance()
    private val userId: String
        get() = auth.currentUser?.uid.orEmpty()

    fun getFavArticles() {
        viewModelScope.launch {
            _favState.value = FavState.Loading
            when (val result = articleRepository.getFavArticles(userId)) {
                is Resource.Success -> {
                    _favState.value = FavState.Data(result.data)
                }

                is Resource.Error -> {
                    _favState.value = FavState.Error(result.throwable)
                }
            }
        }
    }

    fun deleteArticleFromFav(article: ArticleUI) {
        viewModelScope.launch {
            articleRepository.deleteArticleFromFav(article)
            getFavArticles()
        }
    }
}

sealed interface FavState {
    object Loading : FavState
    data class Data(val articles: List<ArticleUI>) : FavState
    data class Error(val throwable: Throwable) : FavState
}