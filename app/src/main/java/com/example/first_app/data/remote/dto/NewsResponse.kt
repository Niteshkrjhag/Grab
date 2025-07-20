package com.example.first_app.data.remote.dto

import com.example.first_app.domain.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)