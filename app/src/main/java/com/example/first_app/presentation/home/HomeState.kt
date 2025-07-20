package com.example.first_app.presentation.home


data class HomeState(
    val newsTicker: String = "",
    val isLoading: Boolean = false,
)