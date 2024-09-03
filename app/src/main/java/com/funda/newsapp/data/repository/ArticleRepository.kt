package com.funda.newsapp.data.repository

import com.funda.newsapp.common.Resource
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.data.source.local.ArticleDao
import com.funda.newsapp.data.source.remote.ArticleService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleService: ArticleService,
    private val articleDao: ArticleDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    //Get all articles
    suspend fun getArticles(
        event: List<Int>? = null,
        hasEvent: Boolean? = null,
        hasLaunch: Boolean? = null,
        isFeatured: Boolean? = null,
        launch: List<String>? = null,
        limit: Int? = null,
        newsSite: String? = null,
        newsSiteExclude: String? = null,
        offset: Int? = null,
        ordering: String? = null,
        publishedAtGt: String? = null,
        publishedAtGte: String? = null,
        publishedAtLt: String? = null,
        publishedAtLte: String? = null,
        search: String? = null,
        summaryContains: String? = null,
        summaryContainsAll: String? = null,
        summaryContainsOne: String? = null,
        titleContains: String? = null,
        titleContainsAll: String? = null,
        titleContainsOne: String? = null,
        updatedAtGt: String? = null,
        updatedAtGte: String? = null,
        updatedAtLt: String? = null,
        updatedAtLte: String? = null
    ): Resource<List<ArticleUI>> {
        return try {
            val response = articleService.getArticles(
                event, hasEvent, hasLaunch, isFeatured, launch, limit, newsSite, newsSiteExclude, offset, ordering,
                publishedAtGt, publishedAtGte, publishedAtLt, publishedAtLte, search, summaryContains,
                summaryContainsAll, summaryContainsOne, titleContains, titleContainsAll, titleContainsOne,
                updatedAtGt, updatedAtGte, updatedAtLt, updatedAtLte
            )
            Resource.Success(response.results.map { it.mapToArticleUI() })
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    //Article Detail
    suspend fun getArticleDetail(id: Int): Resource<ArticleUI> {
        return try {
            val article = withContext(Dispatchers.IO) {
                articleService.getArticleDetail(id)
            }
            Resource.Success(article.mapToArticleUI())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    //Delete article from fav
    suspend fun deleteArticleFromFav(article: ArticleUI) {
        withContext(Dispatchers.IO) {
            articleDao.deleteFavArticle(article.mapToArticleEntity())
        }
    }

    //Get fav articles
    suspend fun getFavArticles(userId: String): Resource<List<ArticleUI>> {
        return try {
            val articles = withContext(Dispatchers.IO) {
                articleDao.getArticlesByUser(userId)
            }
            Resource.Success(articles.map { it.mapToArticleUI() })
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    //Add article to fav
    suspend fun addArticlesToFav(article: ArticleUI, userId: String) {
        withContext(Dispatchers.IO) {
            val entity = article.mapToArticleEntity().copy(userId = userId)
            articleDao.addFavArticle(entity)
        }
    }

}
