package com.example.first_app.domain.repository

import androidx.paging.PagingData
import com.example.first_app.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    // PagingData helps to extract one article at a time out of millions of article in api
    fun getNews(sources: List<String>): Flow<PagingData<Article>>
    fun searchNews(searchQuery: String, sources: List<String>): Flow<PagingData<Article>>

    suspend fun upsertArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun getArticles(): Flow<List<Article>>

    suspend fun getArticle(url: String): Article?
}