package com.example.first_app.domain.usecase.news


import com.example.first_app.data.local.NewsDao
import com.example.first_app.domain.model.Article
import javax.inject.Inject

class GetSavedArticle @Inject constructor(
    private val newsDao: NewsDao
) {

    suspend operator fun invoke(url: String): Article?{
        return newsDao.getArticle(url = url)
    }

}