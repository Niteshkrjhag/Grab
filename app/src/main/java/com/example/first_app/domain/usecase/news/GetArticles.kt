package com.example.first_app.domain.usecase.news


import com.example.first_app.data.local.NewsDao
import com.example.first_app.domain.model.Article
import kotlinx.coroutines.flow.Flow

class GetArticles(
    private val newsDao: NewsDao
) {

    operator fun invoke(): Flow<List<Article>>{
        return newsDao.getArticles()
    }

}