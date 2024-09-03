package com.funda.newsapp

import com.funda.newsapp.common.Resource
import com.funda.newsapp.data.model.Article
import com.funda.newsapp.data.repository.ArticleRepository
import com.funda.newsapp.data.source.local.ArticleDao
import com.funda.newsapp.data.source.remote.ArticleService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ArticleRepositoryTest {

    @Mock
    private lateinit var articleService: ArticleService

    @Mock
    private lateinit var articleDao: ArticleDao

    private lateinit var repository: ArticleRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = ArticleRepository(articleService, articleDao, testDispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getArticleDetail returns data`() = testScope.runTest {
        // Given
        val article = Article(1, "Title", "url", "imageUrl", "newsSite", "Summary", "2024-06-26T09:34:35.137Z", "2024-06-26T09:34:35.137Z", false, emptyList(), emptyList())
        `when`(articleService.getArticleDetail(1)).thenReturn(article)

        // When
        val result = repository.getArticleDetail(1)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(article.mapToArticleUI(), (result as Resource.Success).data)
    }

    @Test
    fun `getArticleDetail returns error`() = testScope.runTest {
        // Given
        val errorMessage = "Error"
        `when`(articleService.getArticleDetail(1)).thenThrow(RuntimeException(errorMessage))

        // When
        val result = repository.getArticleDetail(1)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).throwable.message)
    }
}
