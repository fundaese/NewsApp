package com.funda.newsapp.data.model

data class ArticleUI(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val summary: String,
    val publishedAt: String?
) {
    fun mapToArticleEntity(): ArticleEntity {
        return ArticleEntity(
            id = id,
            title = title,
            url = "", // Provide a default or actual URL if needed
            imageUrl = imageUrl,
            newsSite = "", // Provide a default or actual news site if needed
            summary = summary,
            publishedAt = publishedAt,
            updatedAt = "", // Provide a default or actual update time if needed
            featured = false // Provide a default or actual featured value if needed
        )
    }
}
