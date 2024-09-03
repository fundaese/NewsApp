package com.funda.newsapp.data.model

import android.os.Parcelable
import com.funda.newsapp.common.formatDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: Int,
    val title: String,
    val url: String,
    val image_url: String,
    val newsSite: String,
    val summary: String,
    val published_at: String,
    val updatedAt: String,
    val featured: Boolean,
    val launches: List<Launch>,
    val events: List<Event>
) : Parcelable {
    fun mapToArticleUI(): ArticleUI {
        return ArticleUI(
            id = id ?: 1,
            title = title,
            imageUrl = image_url,
            summary = summary,
            publishedAt = formatDate(published_at)
        )
    }
}

@Parcelize
data class Launch(
    val launchId: String,
    val provider: String
):Parcelable

@Parcelize
data class Event(
    val eventId: Int,
    val provider: String
):Parcelable