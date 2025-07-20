package com.example.first_app.presentation.bookmark

import com.example.first_app.domain.model.Article


data class BookmarkState(
    val articles: List<Article> = emptyList()
)