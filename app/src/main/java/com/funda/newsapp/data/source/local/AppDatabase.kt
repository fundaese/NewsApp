package com.funda.newsapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.funda.newsapp.data.model.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}