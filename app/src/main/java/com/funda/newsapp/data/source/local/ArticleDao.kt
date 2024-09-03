package com.funda.newsapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.funda.newsapp.data.model.ArticleEntity

@Dao
interface ArticleDao {

    @Query("SELECT * FROM fav_articles WHERE user_id = :userId")
    fun getArticlesByUser(userId: String): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavArticle(article: ArticleEntity): Long

    @Delete
    fun deleteFavArticle(article: ArticleEntity): Int
}
