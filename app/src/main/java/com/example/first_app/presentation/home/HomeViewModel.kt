package com.example.first_app.presentation.home


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.first_app.domain.usecase.news.GetNews
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetNews
): ViewModel() {

    var state = mutableStateOf(HomeState())
        private set

    private val sources = listOf(
        "Bloomberg",
        "Business Insider",
        "Crypto Coins News",
        "Engadget",
        "Hacker News",
        "New Scientist",
        "Financial Post",
        "Fortune",
        "InfoMoney",
        "the-verge",
        "coin-desk",
        "hacker-noon",
        "google-news-in",
        "cnbc",
        "crypto-coins-news"
    ).filter { it.isNotBlank() }

    val news = getNewsUseCase(
        sources = sources.shuffled(Random(System.currentTimeMillis())) // Shuffle sources
    ).cachedIn(viewModelScope)

}