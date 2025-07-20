package com.example.first_app.domain.usecase.news

import com.example.first_app.data.local.NewsDao
import com.example.first_app.domain.model.Article
import javax.inject.Inject


class UpsertArticle @Inject constructor(
    private val newsDao: NewsDao
) {

    suspend operator fun invoke(article: Article){
        newsDao.upsert(article = article)
    }

}