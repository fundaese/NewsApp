package com.funda.newsapp.data.source.remote

import com.funda.newsapp.common.Constants.Endpoint.GET_ARTICLES
import com.funda.newsapp.common.Constants.Endpoint.GET_ARTICLES_DETAIL
import com.funda.newsapp.data.model.Article
import com.funda.newsapp.data.model.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleService {
    @GET(GET_ARTICLES)
    suspend fun getArticles(
        @Query("event") event: List<Int>? = null,
        @Query("has_event") hasEvent: Boolean? = null,
        @Query("has_launch") hasLaunch: Boolean? = null,
        @Query("is_featured") isFeatured: Boolean? = null,
        @Query("launch") launch: List<String>? = null,
        @Query("limit") limit: Int? = null,
        @Query("news_site") newsSite: String? = null,
        @Query("news_site_exclude") newsSiteExclude: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("ordering") ordering: String? = null,
        @Query("published_at_gt") publishedAtGt: String? = null,
        @Query("published_at_gte") publishedAtGte: String? = null,
        @Query("published_at_lt") publishedAtLt: String? = null,
        @Query("published_at_lte") publishedAtLte: String? = null,
        @Query("search") search: String? = null,
        @Query("summary_contains") summaryContains: String? = null,
        @Query("summary_contains_all") summaryContainsAll: String? = null,
        @Query("summary_contains_one") summaryContainsOne: String? = null,
        @Query("title_contains") titleContains: String? = null,
        @Query("title_contains_all") titleContainsAll: String? = null,
        @Query("title_contains_one") titleContainsOne: String? = null,
        @Query("updated_at_gt") updatedAtGt: String? = null,
        @Query("updated_at_gte") updatedAtGte: String? = null,
        @Query("updated_at_lt") updatedAtLt: String? = null,
        @Query("updated_at_lte") updatedAtLte: String? = null
    ): ArticleResponse

    @GET(GET_ARTICLES_DETAIL)
    suspend fun getArticleDetail(@Path("id") id:Int) : Article
}