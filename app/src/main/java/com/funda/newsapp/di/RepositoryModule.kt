package com.example.funstore.di

import com.funda.newsapp.data.repository.ArticleRepository
import com.funda.newsapp.data.source.local.ArticleDao
import com.funda.newsapp.data.source.remote.ArticleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(articleService: ArticleService, articleDao: ArticleDao) : ArticleRepository =
        ArticleRepository(articleService,articleDao)

}