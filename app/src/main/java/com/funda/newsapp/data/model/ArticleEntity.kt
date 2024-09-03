package com.funda.newsapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_articles")
data class ArticleEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "url")
    var url: String? = null,

    @ColumnInfo(name = "image_url")
    var imageUrl: String? = null,

    @ColumnInfo(name = "newsSite")
    var newsSite: String? = null,

    @ColumnInfo(name = "summary")
    var summary: String? = null,

    @ColumnInfo(name = "published_at")
    var publishedAt: String? = null,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: String? = null,

    @ColumnInfo(name = "featured")
    var featured: Boolean? = null,

    @ColumnInfo(name = "user_id")
    var userId: String? = null
) {
    fun mapToArticleUI(): ArticleUI {
        return ArticleUI(
            id = id ?: 1,
            title = title.orEmpty(),
            imageUrl = imageUrl.orEmpty(),
            summary = summary.orEmpty(),
            publishedAt = publishedAt
        )
    }
}
